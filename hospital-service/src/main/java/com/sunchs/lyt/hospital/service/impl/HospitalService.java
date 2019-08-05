package com.sunchs.lyt.hospital.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.sunchs.lyt.db.business.entity.*;
import com.sunchs.lyt.db.business.service.impl.*;
import com.sunchs.lyt.framework.bean.PagingList;
import com.sunchs.lyt.framework.util.FormatUtil;
import com.sunchs.lyt.framework.util.ObjectUtil;
import com.sunchs.lyt.framework.util.PagingUtil;
import com.sunchs.lyt.framework.util.UserThreadUtil;
import com.sunchs.lyt.hospital.bean.*;
import com.sunchs.lyt.hospital.enums.ExtendTypeEnum;
import com.sunchs.lyt.hospital.enums.HospitalStatusEnum;
import com.sunchs.lyt.hospital.enums.OfficeTypeEnum;
import com.sunchs.lyt.hospital.service.IHospitalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;

@Service
public class HospitalService implements IHospitalService {

    @Autowired
    private HospitalServiceImpl hospitalService;

    @Autowired
    private HospitalOfficeServiceImpl hospitalOfficeService;

    @Autowired
    private HospitalExtendServiceImpl hospitalExtendService;

    @Autowired
    private RegionServiceImpl regionService;

    @Autowired
    private HospitalRegionServiceImpl hospitalRegionService;

    @Autowired
    private ItemOfficeServiceImpl itemOfficeService;

    @Override
    public void save(HospitalParam param) {
        System.out.println(param);
        Hospital data = new Hospital();
        data.setId(param.getId());
        data.setHospitalName(param.getHospitalName());
        data.setHospitalType(param.getHospitalType());
        data.setHospitalProperty(param.getHospitalProperty());
        data.setSubjection(param.getSubjection());
        data.setAddress(param.getAddress());
        data.setContacts(param.getContacts());
        data.setContactInfo(param.getContactInfo());
        data.setOperationName(param.getOperationName());
        data.setOperationPhone(param.getOperationPhone());
        data.setOpenBeds(param.getOpenBeds());
        data.setRemarks(param.getRemarks());
        data.setUpdateId(UserThreadUtil.getUserId());
        data.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        if (param.getId() == 0) {
            data.setCreateId(UserThreadUtil.getUserId());
            data.setCreateTime(new Timestamp(System.currentTimeMillis()));
        }
        if (hospitalService.insertOrUpdate(data)) {
            // 地区
            setRegion(data.getId(), param.getRegion());
            // 门诊科室
            setOffice(data.getId(), OfficeTypeEnum.Outpatient.value, param.getOutpatientOffice());
            // 住院科室
            setOffice(data.getId(), OfficeTypeEnum.Inpatient.value, param.getInpatientOffice());
            // 特殊科室
            setOffice(data.getId(), OfficeTypeEnum.Special.value, param.getSpecialOffice());
            // 分院信息
            setExtend(data.getId(), ExtendTypeEnum.Branch.value, param.getHospitalBranch());
            // 门诊类别
            setExtend(data.getId(), ExtendTypeEnum.OutpatientType.value, param.getOutpatientType());
            // 挂号方式
            setExtend(data.getId(), ExtendTypeEnum.RegistrationMode.value, param.getRegistrationMode());
            // 员工信息
            setExtend(data.getId(), ExtendTypeEnum.EmployeeInfo.value, param.getEmployeeInfo());
        }
    }

    @Override
    public PagingList<HospitalData> getPageList(HospitalParam param) {
        List<HospitalData> list = new ArrayList<>();
        Wrapper<Hospital> w = new EntityWrapper<>();
        Page<Hospital> page = hospitalService.selectPage(new Page<>(param.getPageNow(), param.getPageSize()), w);
        page.getRecords().forEach(row-> list.add(getHospitalInfo(row)));
        return PagingUtil.getData(list, page.getSize(), param.getPageNow(), param.getPageSize());
    }

    @Override
    public HospitalData getById(int hospitalId) {
        Hospital hospital = hospitalService.selectById(hospitalId);
        return getHospitalInfo(hospital);
    }

    @Override
    public List<Map<String, Object>> getSelectData() {
        List<Map<String, Object>> list = new ArrayList<>();
        Wrapper<Hospital> w = new EntityWrapper<>();
        w.eq(Hospital.STATUS, 1);
        w.orderBy(Role.ID, true);
        List<Hospital> res = hospitalService.selectList(w);
        res.forEach(row -> {
            Map<String, Object> m = new HashMap<>();
            m.put("id", row.getId());
            m.put("title", row.getHospitalName());
            list.add(m);
        });
        return list;
    }

    @Override
    public void updateStatus(HospitalParam param) {
        Hospital data = new Hospital();
        data.setId(param.getId());
        data.setStatus(param.getStatus());
        hospitalService.updateById(data);
    }

    @Override
    public List<Map<String, Object>> getUsableList() {
        Wrapper<Hospital> w = new EntityWrapper<>();
//        w.setSqlSelect(Hospital.ID, Hospital.HOSPITAL_NAME);
        w.eq(User.STATUS, 1);
        w.orderBy(User.ID, false);
        List<Hospital> hospitalList = hospitalService.selectList(w);
        List<Map<String, Object>> list = new ArrayList<>();
        hospitalList.forEach(row -> {
            Map<String, Object> m = new HashMap<>();
            m.put("id", row.getId());
            m.put("hospitalName", row.getHospitalName());
            list.add(m);
        });
        return list;
    }

    @Override
    public List<Map<String, Object>> getOfficeList(int hospitalId) {
        Wrapper<HospitalOffice> wrapper = new EntityWrapper<HospitalOffice>()
                .eq(HospitalOffice.HOSPITAL_ID, hospitalId);
        List<HospitalOffice> officeList = hospitalOfficeService.selectList(wrapper);
        List<Map<String, Object>> list = new ArrayList<>();
        officeList.forEach(row -> {
            Map<String, Object> m = new HashMap<>();
            m.put("id", row.getId());
            m.put("type", row.getType());
            m.put("title", row.getTitle());
            m.put("quantity", row.getQuantity());
            list.add(m);
        });
        return list;
    }

    @Override
    public List<Map<String, Object>> getNoBindOfficeList(int itemId, int hospitalId) {
        List<Integer> delIds = new ArrayList<>();
        Wrapper<ItemOffice> w = new EntityWrapper<ItemOffice>()
                .eq(ItemOffice.ITEM_ID, itemId);
        itemOfficeService.selectList(w).forEach(o -> delIds.add(o.getOfficeId()));

        Wrapper<HospitalOffice> wrapper = new EntityWrapper<HospitalOffice>()
                .eq(HospitalOffice.HOSPITAL_ID, hospitalId)
                .notIn(HospitalOffice.ID, delIds);
        List<HospitalOffice> officeList = hospitalOfficeService.selectList(wrapper);
        List<Map<String, Object>> list = new ArrayList<>();
        officeList.forEach(row -> {
            Map<String, Object> m = new HashMap<>();
            m.put("id", row.getId());
            m.put("type", row.getType());
            m.put("title", row.getTitle());
            m.put("quantity", row.getQuantity());
            list.add(m);
        });
        return list;
    }

    private HospitalData getHospitalInfo(Hospital hospital) {
        HospitalData res = ObjectUtil.copy(hospital, HospitalData.class);
        // 地区
        res.setRegion(getRegion(res.getId()));
        // 门诊科室
        res.setOutpatientOffice(getHospitalOffice(res.getId(), OfficeTypeEnum.Outpatient.value));
        // 住院科室
        res.setInpatientOffice(getHospitalOffice(res.getId(), OfficeTypeEnum.Inpatient.value));
        // 特殊科室
        res.setSpecialOffice(getHospitalOffice(res.getId(), OfficeTypeEnum.Special.value));
        // 分院信息
        res.setHospitalBranch(getHospitalExtend(res.getId(), ExtendTypeEnum.Branch.value));
        // 门诊类别
        res.setOutpatientType(getHospitalExtend(res.getId(), ExtendTypeEnum.OutpatientType.value));
        // 挂号方式
        res.setRegistrationMode(getHospitalExtend(res.getId(), ExtendTypeEnum.RegistrationMode.value));
        // 员工信息
        res.setEmployeeInfo(getHospitalExtend(res.getId(), ExtendTypeEnum.EmployeeInfo.value));

        // 状态
        res.setStatusName(HospitalStatusEnum.get(res.getStatus()));

        switch (res.getHospitalType()) {
            case 1:
                res.setHospitalTypeName("三级综合");
                break;
            case 2:
                res.setHospitalTypeName("二级综合");
                break;
            case 3:
                res.setHospitalTypeName("专科医院");
                break;
            default:
                res.setHospitalTypeName("无类型");
        }

        switch (res.getHospitalProperty()) {
            case 1:
                res.setHospitalPropertyName("公立医院");
                break;
            case 2:
                res.setHospitalPropertyName("私立医院");
                break;
            case 3:
                res.setHospitalPropertyName("军队医院");
                break;
            default:
                res.setHospitalPropertyName("无性质");
        }

        switch (res.getSubjection()) {
            case 1:
                res.setSubjectionName("省属");
                break;
            case 2:
                res.setSubjectionName("市属");
                break;
            case 3:
                res.setSubjectionName("区/县属");
                break;
            default:
                res.setSubjectionName("无");
        }

        res.setUpdateTimeName(FormatUtil.dateTime(res.getUpdateTime()));
        return res;
    }

    private List<HospitalOfficeData> getHospitalOffice(int hospitalId, int officeType) {
        Wrapper<HospitalOffice> where = new EntityWrapper<>();
        where.eq(HospitalOffice.HOSPITAL_ID, hospitalId);
        where.eq(HospitalOffice.TYPE, officeType);
        List<HospitalOffice> hospitalOffices = hospitalOfficeService.selectList(where);
        List<HospitalOfficeData> list = new ArrayList<>();
        hospitalOffices.forEach(office->{
            HospitalOfficeData o = new HospitalOfficeData();
            o.setId(office.getId());
            o.setName(office.getTitle());
            o.setQuantity(office.getQuantity());
            list.add(o);
        });
        return list;
    }

    private List<HospitalExtendData> getHospitalExtend(int hospitalId, int officeType) {
        Wrapper<HospitalExtend> where = new EntityWrapper<>();
        where.eq(HospitalExtend.HOSPITAL_ID, hospitalId);
        where.eq(HospitalExtend.TYPE, officeType);
        List<HospitalExtend> hospitalExtends = hospitalExtendService.selectList(where);
        List<HospitalExtendData> list = new ArrayList<>();
        hospitalExtends.forEach(extend->{
            HospitalExtendData data = new HospitalExtendData();
            data.setId(extend.getId());
            data.setContent(extend.getContent());
            data.setContent2(extend.getContent2());
            list.add(data);
        });
        return list;
    }

    private List<Integer> getRegion(int hospitalId) {
        List<Integer> ids = new ArrayList<>();
        Wrapper<HospitalRegion> where = new EntityWrapper<>();
        where.eq(HospitalRegion.HOSPITAL_ID, hospitalId);
        List<HospitalRegion> hospitalOffices = hospitalRegionService.selectList(where);
        hospitalOffices.forEach(region->{
            ids.add(region.getRegionId());
        });
        return ids;
    }

    /**
     * 科室
     */
    private void setOffice(int hospitalId, int officeType, List<HospitalOfficeParam> paramList) {
        // 删除历史数据
        List<Integer> ids = new ArrayList<>();
        for (HospitalOfficeParam row : paramList) {
            if (row.getId() > 0) {
                ids.add(row.getId());
            }
        }
        Wrapper<HospitalOffice> w = new EntityWrapper<>();
        w.eq(HospitalOffice.HOSPITAL_ID, hospitalId);
        w.eq(HospitalOffice.TYPE, officeType);
        w.notIn(HospitalOffice.ID, ids);
        hospitalOfficeService.delete(w);

        // 添加新数据
        for (HospitalOfficeParam row : paramList) {
            HospitalOffice data = new HospitalOffice();
            data.setId(row.getId());
            data.setHospitalId(hospitalId);
            data.setType(officeType);
            data.setTitle(row.getName());
            data.setQuantity(row.getQuantity());
            data.setUpdateId(UserThreadUtil.getUserId());
            data.setUpdateTime(new Timestamp(System.currentTimeMillis()));
            hospitalOfficeService.insertOrUpdate(data);
        }
    }

    /**
     * 设置扩展信息
     */
    private void setExtend(int hospitalId, int extendType, List<HospitalExtendParam> list) {
        // 删除历史数据
        List<Integer> ids = new ArrayList<>();
        for (HospitalExtendParam row : list) {
            if (row.getId() > 0) {
                ids.add(row.getId());
            }
        }
        Wrapper<HospitalExtend> w = new EntityWrapper<>();
        w.eq(HospitalExtend.HOSPITAL_ID, hospitalId);
        w.eq(HospitalExtend.TYPE, extendType);
        w.notIn(HospitalExtend.ID, ids);
        hospitalExtendService.delete(w);

        // 添加新数据
        for (HospitalExtendParam res : list) {
            HospitalExtend data = new HospitalExtend();
            data.setId(res.getId());
            data.setHospitalId(hospitalId);
            data.setType(extendType);
            data.setContent(res.getContent());
            data.setContent2(res.getContent2());
            data.setUpdateId(UserThreadUtil.getUserId());
            data.setUpdateTime(new Timestamp(System.currentTimeMillis()));
            hospitalExtendService.insertOrUpdate(data);
        }
    }

    /**
     * 插入区域
     */
    private void setRegion(int hospitalId, List<Integer> regionList) {
        // 清历史数据
        Wrapper<HospitalRegion> w = new EntityWrapper<>();
        w.eq(HospitalRegion.HOSPITAL_ID, hospitalId);
        hospitalRegionService.delete(w);
        // 插入新数据
        if (Objects.nonNull(regionList)) {
            for (Integer regionId : regionList) {
                Region region = regionService.selectById(regionId);
                if (Objects.nonNull(region)) {
                    HospitalRegion hospitalRegion = new HospitalRegion();
                    hospitalRegion.setHospitalId(hospitalId);
                    hospitalRegion.setPid(region.getPid());
                    hospitalRegion.setRegionId(region.getRegionId());
                    hospitalRegion.setTitle(region.getTitle());
                    hospitalRegionService.insert(hospitalRegion);
                }
            }
        }
    }
}
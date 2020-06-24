package com.sunchs.lyt.hospital.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.sunchs.lyt.db.business.entity.Hospital;
import com.sunchs.lyt.db.business.entity.HospitalComplaint;
import com.sunchs.lyt.db.business.entity.HospitalComplaintType;
import com.sunchs.lyt.db.business.entity.HospitalOffice;
import com.sunchs.lyt.db.business.service.impl.HospitalComplaintServiceImpl;
import com.sunchs.lyt.db.business.service.impl.HospitalComplaintTypeServiceImpl;
import com.sunchs.lyt.db.business.service.impl.HospitalOfficeServiceImpl;
import com.sunchs.lyt.db.business.service.impl.HospitalServiceImpl;
import com.sunchs.lyt.framework.bean.PagingList;
import com.sunchs.lyt.framework.enums.OfficeTypeEnum;
import com.sunchs.lyt.framework.util.FormatUtil;
import com.sunchs.lyt.framework.util.PagingUtil;
import com.sunchs.lyt.hospital.bean.ComplaintParam;
import com.sunchs.lyt.hospital.bean.HospitalComplaintData;
import com.sunchs.lyt.hospital.exception.HospitalException;
import com.sunchs.lyt.hospital.service.IComplaintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ComplaintService implements IComplaintService {

    @Autowired
    private HospitalComplaintServiceImpl hospitalComplaintService;

    @Autowired
    private HospitalComplaintTypeServiceImpl hospitalComplaintTypeService;

    @Autowired
    private HospitalServiceImpl hospitalService;

    @Autowired
    private HospitalOfficeServiceImpl hospitalOfficeService;

    @Override
    public void save(ComplaintParam param) {
        // 参数过滤
        param.filter();
        // 保存数据
        try {
            HospitalComplaint data = new HospitalComplaint();
            data.setHospitalId(param.getHospitalId());
            data.setOfficeTypeId(param.getOfficeTypeId());
            data.setOfficeId(param.getOfficeId());
            data.setTypeId(param.getTypeId());
            data.setName(param.getName());
            data.setTel(param.getTel());
            data.setNumber(param.getNumber());
            data.setRespondent(param.getRespondent());
            data.setContent(param.getContent());
            data.setCreateTime(new Date());
            hospitalComplaintService.insert(data);
        } catch (Exception e) {
            throw new HospitalException("投诉信息保存失败:" + e.getMessage());
        }
    }

    @Override
    public PagingList<HospitalComplaintData> getList(ComplaintParam param) {
        Wrapper<HospitalComplaint> wrapper = new EntityWrapper<>();
        // 姓名
        if (Objects.nonNull(param.getName()) && param.getName().length() > 0) {
            wrapper.eq(HospitalComplaint.NAME, param.getName());
        }
        // 电话
        if (Objects.nonNull(param.getTel()) && param.getTel().length() > 0) {
            wrapper.eq(HospitalComplaint.TEL, param.getTel());
        }
        // 科室
        if (param.getOfficeId() != 0) {
            wrapper.eq(HospitalComplaint.OFFICE_ID, param.getOfficeId());
        }
        // 时间段
        if (Objects.nonNull(param.getStartTime()) && param.getStartTime().length() > 0) {
            wrapper.ge(HospitalComplaint.CREATE_TIME, param.getStartTime());
        }
        if (Objects.nonNull(param.getEndTime()) && param.getEndTime().length() > 0) {
            wrapper.le(HospitalComplaint.CREATE_TIME, param.getEndTime());
        }
        Page<HospitalComplaint> limit = new Page<>(param.getPageNow(), param.getPageSize());
        Page<HospitalComplaint> page = hospitalComplaintService.selectPage(limit, wrapper);
        List<HospitalComplaintData> list = formatData(page.getRecords());
        return PagingUtil.getData(list, page.getTotal(), page.getCurrent(), page.getSize());
    }

    @Override
    public List<HospitalComplaintType> getTypeList(Integer hospitalId) {
        Wrapper<HospitalComplaintType> wrapper = new EntityWrapper<HospitalComplaintType>()
                .setSqlSelect(
                        HospitalComplaintType.ID,
                        HospitalComplaintType.TITLE
                )
                .in(HospitalComplaintType.HOSPITAL_ID, Arrays.asList(hospitalId, 0));
        return hospitalComplaintTypeService.selectList(wrapper);
    }

    private List<HospitalComplaintData> formatData(List<HospitalComplaint> pageList) {
        List<HospitalComplaintData> list = new ArrayList<>();
        // 医院
        List<Integer> hospitalIds = pageList.stream().map(HospitalComplaint::getHospitalId).distinct().collect(Collectors.toList());
        Wrapper<Hospital> hospitalWrapper = new EntityWrapper<Hospital>()
                .setSqlSelect(
                        Hospital.ID,
                        Hospital.HOSPITAL_NAME.concat(" as hospitalName")
                )
                .in(Hospital.ID, hospitalIds);
        Map<Integer, String> hospitalMap = hospitalService.selectList(hospitalWrapper).stream().collect(Collectors.toMap(Hospital::getId, Hospital::getHospitalName));

        // 科室
        List<Integer> officeIds = pageList.stream().map(HospitalComplaint::getOfficeId).distinct().collect(Collectors.toList());
        Wrapper<HospitalOffice> officeWrapper = new EntityWrapper<HospitalOffice>()
                .setSqlSelect(
                        HospitalOffice.ID,
                        HospitalOffice.TITLE
                )
                .in(HospitalOffice.ID, officeIds);
        Map<Integer, String> hospitalOfficeMap = hospitalOfficeService.selectList(officeWrapper).stream().collect(Collectors.toMap(HospitalOffice::getId, HospitalOffice::getTitle));

        // 投诉类型
        List<Integer> typeIds = pageList.stream().map(HospitalComplaint::getTypeId).distinct().collect(Collectors.toList());
        Wrapper<HospitalComplaintType> typeWrapper = new EntityWrapper<HospitalComplaintType>()
                .setSqlSelect(
                        HospitalComplaintType.ID,
                        HospitalComplaintType.TITLE
                )
                .in(HospitalComplaintType.ID, typeIds);
        Map<Integer, String> typeMap = hospitalComplaintTypeService.selectList(typeWrapper).stream().collect(Collectors.toMap(HospitalComplaintType::getId, HospitalComplaintType::getTitle));

        pageList.forEach(row -> {
            HospitalComplaintData data = new HospitalComplaintData();
            data.setId(row.getId());
            data.setHospitalId(row.getHospitalId());
            data.setHospitalName(hospitalMap.get(row.getHospitalId()));
            data.setOfficeTypeId(row.getOfficeTypeId());
            data.setOfficeTypeName(OfficeTypeEnum.get(row.getOfficeTypeId()));
            data.setOfficeId(row.getOfficeId());
            data.setOfficeName(hospitalOfficeMap.get(row.getOfficeId()));
            data.setTypeId(row.getTypeId());
            data.setTypeName(typeMap.get(row.getTypeId()));
            data.setName(row.getName());
            data.setTel(row.getTel());
            data.setNumber(row.getNumber());
            data.setRespondent(row.getRespondent());
            data.setContent(row.getContent());
            data.setCreateTime(FormatUtil.dateTime(row.getCreateTime()));
            list.add(data);
        });
        return list;
    }
}

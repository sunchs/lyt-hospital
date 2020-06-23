package com.sunchs.lyt.hospital.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.sunchs.lyt.db.business.entity.HospitalComplaint;
import com.sunchs.lyt.db.business.entity.HospitalComplaintType;
import com.sunchs.lyt.db.business.service.impl.HospitalComplaintServiceImpl;
import com.sunchs.lyt.db.business.service.impl.HospitalComplaintTypeServiceImpl;
import com.sunchs.lyt.framework.bean.PagingList;
import com.sunchs.lyt.framework.util.PagingUtil;
import com.sunchs.lyt.hospital.bean.ComplaintParam;
import com.sunchs.lyt.hospital.exception.HospitalException;
import com.sunchs.lyt.hospital.service.IComplaintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class ComplaintService implements IComplaintService {

    @Autowired
    private HospitalComplaintServiceImpl hospitalComplaintService;

    @Autowired
    private HospitalComplaintTypeServiceImpl hospitalComplaintTypeService;

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
    public PagingList<HospitalComplaint> getList(ComplaintParam param) {
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
        return PagingUtil.getData(page);
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
}

package com.sunchs.lyt.hospital.service;

import com.sunchs.lyt.db.business.entity.HospitalComplaint;
import com.sunchs.lyt.db.business.entity.HospitalComplaintType;
import com.sunchs.lyt.framework.bean.PagingList;
import com.sunchs.lyt.hospital.bean.ComplaintParam;

import java.util.List;

public interface IComplaintService {

    /**
     * 添加 投诉信息
     */
    void save(ComplaintParam param);

    /**
     * 获取列表信息
     */
    PagingList<HospitalComplaint> getList(ComplaintParam param);

    /**
     * 获取列表信息
     */
    List<HospitalComplaintType> getTypeList(Integer hospitalId);
}

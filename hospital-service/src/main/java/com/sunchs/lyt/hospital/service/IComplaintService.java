package com.sunchs.lyt.hospital.service;

import com.sunchs.lyt.db.business.entity.HospitalComplaintType;
import com.sunchs.lyt.framework.bean.PagingList;
import com.sunchs.lyt.hospital.bean.ComplaintParam;
import com.sunchs.lyt.hospital.bean.HospitalComplaintData;

import java.util.List;

public interface IComplaintService {

    /**
     * 添加 投诉信息
     */
    void save(ComplaintParam param);

    /**
     * 获取列表信息
     */
    PagingList<HospitalComplaintData> getList(ComplaintParam param);

    /**
     * 获取列表信息
     */
    List<HospitalComplaintType> getTypeList(Integer hospitalId);

    /**
     * 导出投诉报表文件
     */
    String outputFile(Integer hospitalId, String startTime, String endTime);
}

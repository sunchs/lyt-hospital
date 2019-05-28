package com.sunchs.lyt.hospital.service;

import com.sunchs.lyt.framework.bean.PagingList;
import com.sunchs.lyt.hospital.bean.HospitalData;
import com.sunchs.lyt.hospital.bean.HospitalParam;

public interface IHospitalService {

    /**
     * 保存数据
     */
    void save(HospitalParam param);

    /**
     * 医院信息分页列表
     */
    PagingList<HospitalData> getPageList(HospitalParam param);

    /**
     * 根据 医院ID 获取医院详情
     */
    HospitalData getById(int hospitalId);
}

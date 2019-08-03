package com.sunchs.lyt.hospital.service;

import com.sunchs.lyt.framework.bean.PagingList;
import com.sunchs.lyt.hospital.bean.HospitalData;
import com.sunchs.lyt.hospital.bean.HospitalParam;

import java.util.List;
import java.util.Map;

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

    /**
     * 获取select数据
     */
    List<Map<String, Object>> getSelectData();

    /**
     * 根据 医院ID 更新状态
     */
    void updateStatus(HospitalParam param);

    /**
     * 获取可用医院列表
     */
    List<Map<String, Object>> getUsableList();

    /**
     * 获取医院科室
     */
    List<Map<String, Object>> getOfficeList(int hospitalId);

    /**
     * 获取未绑定的科室
     */
    List<Map<String, Object>> getNoBindOfficeList(int itemId, int hospitalId);
}

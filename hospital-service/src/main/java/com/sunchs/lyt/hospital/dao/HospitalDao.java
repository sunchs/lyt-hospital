package com.sunchs.lyt.hospital.dao;

import com.sunchs.lyt.hospital.bean.HospitalData;
import com.sunchs.lyt.hospital.bean.HospitalParam;

import java.util.List;
import java.util.Map;

public interface HospitalDao {

    /**
     * 根据 问题ID 获取问题信息
     */
    HospitalData getById(int id);

    /**
     * 添加医院信息
     */
    int insert(Map<String, Object> param);

    /**
     * 修改医院信息
     */
    int update(Map<String, Object> param);

    /**
     * 插入扩展消息
     */
    void insertExtend(Map<String, Object> param);

    /**
     * 删除扩展消息
     */
    void deleteExtend(Map<String, Object> param);

    /**
     * 获取 医院信息 总条数
     */
    int getCount(HospitalParam param);

    /**
     * 获取 医院信息 分页数据
     */
    List<HospitalData> getPageList(HospitalParam param);

    /**
     * 根据 医院ID 获取扩展信息
     */
    List<String> getExtendById(int hospitalId, String type);
}

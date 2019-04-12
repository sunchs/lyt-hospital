package com.sunchs.lyt.hospital.dao;

import java.util.Map;

public interface HospitalDao {

    /**
     * 添加问题
     */
    int insert(Map<String, Object> param);

    /**
     * 修改问题
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
}

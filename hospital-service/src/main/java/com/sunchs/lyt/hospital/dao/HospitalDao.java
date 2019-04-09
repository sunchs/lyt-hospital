package com.sunchs.lyt.hospital.dao;

import java.util.Map;

public interface HospitalDao {

    /**
     * 添加问题
     */
    Integer insert(Map<String, Object> param);

    /**
     * 修改问题
     */
    int update(Map<String, Object> param);
}

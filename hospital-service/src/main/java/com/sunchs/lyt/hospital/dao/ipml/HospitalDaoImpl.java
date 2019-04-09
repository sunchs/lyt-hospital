package com.sunchs.lyt.hospital.dao.ipml;

import com.sunchs.lyt.hospital.dao.HospitalDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Map;


@Repository
public class HospitalDaoImpl implements HospitalDao {

    @Autowired
    private NamedParameterJdbcTemplate db;

    @Override
    public Integer insert(Map<String, Object> param) {
        return null;
    }

    @Override
    public int update(Map<String, Object> param) {
        return 0;
    }
}
package com.sunchs.lyt.hospital.dao.ipml;

import com.sunchs.lyt.hospital.dao.HospitalDao;
import com.sunchs.lyt.hospital.exception.HospitalException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class HospitalDaoImpl implements HospitalDao {

    @Autowired
    private NamedParameterJdbcTemplate db;

    @Override
    public int insert(Map<String, Object> param) {
        try {
            GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
            String sql = "INSERT INTO hospital(`hospital_name`,`hospital_type`,`hospital_property`,`subjection`,`address`,`contacts`," +
                    "`contact_info`,`operation_name`,`operation_phone`,`open_beds`,`remarks`) " +
                    "VALUES(:hospitalName, :hospitalType, :hospitalProperty, :subjection, :address, :contacts, " +
                    ":contactInfo, :operationName, :operationPhone, :openBeds, :remarks)";
            db.update(sql, new MapSqlParameterSource(param), keyHolder);
            return keyHolder.getKey().intValue();
        } catch (Exception e) {
            throw new HospitalException("医院信息写入 --> 异常:" + e.getMessage());
        }
    }

    @Override
    public int update(Map<String, Object> param) {
        try {
            String sql = "UPDATE hospital SET `hospital_name`=:hospitalName,`hospital_type`=:hospitalType,`hospital_property`=:hospitalProperty" +
                    ",`subjection`=:subjection,`address`=:address,`contacts`=:contacts,`contact_info`=:contactInfo,`operation_name`=:operationName" +
                    ",`operation_phone`=:operationPhone,`open_beds`=:openBeds,`remarks`=:remarks WHERE id=:id";
            db.update(sql, new MapSqlParameterSource(param));
            return (int) param.get("id");
        } catch (Exception e) {
            throw new HospitalException("医院扩展信息更新 --> 异常:" + e.getMessage());
        }
    }

    @Override
    public void insertExtend(Map<String, Object> param) {
        try {
            String sql = "INSERT INTO hospital_extend(`hospital_id`,`type`,`content`) VALUES(:hospitalId, :type, :content)";
            db.update(sql, new MapSqlParameterSource(param));
        } catch (Exception e) {
            throw new HospitalException("医院扩展信息写入 --> 异常:" + e.getMessage());
        }
    }

    @Override
    public void deleteExtend(Map<String, Object> param) {
        try {
            String sql = "DELETE FROM hospital_extend WHERE hospital_id=:hospitalId AND type=:type";
            db.update(sql, new MapSqlParameterSource(param));
        } catch (Exception e) {
            throw new HospitalException("医院扩展信息删除 --> 异常:" + e.getMessage());
        }
    }
}
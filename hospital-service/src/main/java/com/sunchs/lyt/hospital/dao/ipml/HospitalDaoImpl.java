package com.sunchs.lyt.hospital.dao.ipml;

import com.sunchs.lyt.framework.util.JsonUtil;
import com.sunchs.lyt.framework.util.PagingUtil;
import com.sunchs.lyt.hospital.bean.HospitalBranchParam;
import com.sunchs.lyt.hospital.bean.HospitalData;
import com.sunchs.lyt.hospital.bean.HospitalParam;
import com.sunchs.lyt.hospital.dao.HospitalDao;
import com.sunchs.lyt.hospital.exception.HospitalException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class HospitalDaoImpl implements HospitalDao {

    @Autowired
    private NamedParameterJdbcTemplate db;

//    @Override
//    public HospitalData getById(int id) {
//        String sql = "SELECT `id`,`hospital_name`,`hospital_type`,`hospital_property`,`subjection`,`address`,`contacts`," +
//                "`contact_info`,`operation_name`,`operation_phone`,`open_beds`,`remarks` FROM hospital WHERE id=:id";
//        MapSqlParameterSource param = new MapSqlParameterSource()
//                .addValue("id", id);
//        HospitalData targetData =  db.queryForObject(sql, param, (ResultSet rs, int rowNum) -> {
//            HospitalData data = new HospitalData();
//            data.setId(rs.getInt("id"));
//            data.setHospitalName(rs.getString("hospital_name"));
//            data.setHospitalType(rs.getInt("hospital_type"));
//            data.setHospitalProperty(rs.getInt("hospital_property"));
//            data.setSubjection(rs.getInt("subjection"));
//            data.setAddress(rs.getString("address"));
//            data.setContacts(rs.getString("contacts"));
//            data.setContactInfo(rs.getString("contact_info"));
//            data.setOperationName(rs.getString("operation_name"));
//            data.setOperationPhone(rs.getString("operation_phone"));
//            data.setOpenBeds(rs.getInt("open_beds"));
//            data.setRemarks(rs.getString("remarks"));
//
//            data.setOutpatientOffice(getExtendById(data.getId(), "outpatient_office"));
//            data.setInpatientOffice(getExtendById(data.getId(), "inpatient_office"));
//            data.setSpecialOffice(getExtendById(data.getId(), "special_office"));
//
//            List<HospitalBranchParam> outpatientOfficeList = new ArrayList<>();
//            List<String> outpatientOffices = getExtendById(data.getId(), "outpatient_office");
//            for (String json : outpatientOffices) {
//                outpatientOfficeList.add(JsonUtil.toObject(json, HospitalBranchParam.class));
//            }
//            data.setHospitalBranch(outpatientOfficeList);
//
//            data.setOutpatientType(getExtendById(data.getId(), "outpatient_type"));
//            data.setRegistrationMode(getExtendById(data.getId(), "registration_mode"));
//            return data;
//        });
//        return targetData;
//    }
//
//    @Override
//    public int insert(Map<String, Object> param) {
//        try {
//            GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
//            String sql = "INSERT INTO hospital(`hospital_name`,`hospital_type`,`hospital_property`,`subjection`,`address`,`contacts`," +
//                    "`contact_info`,`operation_name`,`operation_phone`,`open_beds`,`remarks`) " +
//                    "VALUES(:hospitalName, :hospitalType, :hospitalProperty, :subjection, :address, :contacts, " +
//                    ":contactInfo, :operationName, :operationPhone, :openBeds, :remarks)";
//            db.update(sql, new MapSqlParameterSource(param), keyHolder);
//            return keyHolder.getKey().intValue();
//        } catch (Exception e) {
//            throw new HospitalException("医院信息写入 --> 异常:" + e.getMessage());
//        }
//    }
//
//    @Override
//    public int update(Map<String, Object> param) {
//        try {
//            String sql = "UPDATE hospital SET `hospital_name`=:hospitalName,`hospital_type`=:hospitalType,`hospital_property`=:hospitalProperty" +
//                    ",`subjection`=:subjection,`address`=:address,`contacts`=:contacts,`contact_info`=:contactInfo,`operation_name`=:operationName" +
//                    ",`operation_phone`=:operationPhone,`open_beds`=:openBeds,`remarks`=:remarks WHERE id=:id";
//            db.update(sql, new MapSqlParameterSource(param));
//            return (int) param.get("id");
//        } catch (Exception e) {
//            throw new HospitalException("医院扩展信息更新 --> 异常:" + e.getMessage());
//        }
//    }
//
//    @Override
//    public void insertExtend(Map<String, Object> param) {
//        try {
//            String sql = "INSERT INTO hospital_extend(`hospital_id`,`type`,`content`) VALUES(:hospitalId, :type, :content)";
//            db.update(sql, new MapSqlParameterSource(param));
//        } catch (Exception e) {
//            throw new HospitalException("医院扩展信息写入 --> 异常:" + e.getMessage());
//        }
//    }
//
//    @Override
//    public void deleteExtend(Map<String, Object> param) {
//        try {
//            String sql = "DELETE FROM hospital_extend WHERE hospital_id=:hospitalId AND type=:type";
//            db.update(sql, new MapSqlParameterSource(param));
//        } catch (Exception e) {
//            throw new HospitalException("医院扩展信息删除 --> 异常:" + e.getMessage());
//        }
//    }
//
//    @Override
//    public int getCount(HospitalParam param) {
//        String sql = "SELECT COUNT(*) FROM hospital";
//        Integer integer = db.queryForObject(sql, new MapSqlParameterSource(), Integer.class);
//        return integer.intValue();
//    }
//
//    @Override
//    public List<HospitalData> getPageList(HospitalParam param) {
//        int skip = PagingUtil.getSkip(param.getPageNow(), param.getPageSize());
//        String sql = "SELECT `id` FROM hospital ORDER BY `id` DESC LIMIT :skip,:pageSize";
//        MapSqlParameterSource childParam = new MapSqlParameterSource()
//                .addValue("skip", skip)
//                .addValue("pageSize", param.getPageSize());
//        List<Integer> ids = db.query(sql, childParam, (ResultSet rs, int rowNum) -> rs.getInt("id"));
//        List<HospitalData> result = new ArrayList<>();
//        for (int id : ids) {
//            result.add(getById(id));
//        }
//        return result;
//    }

    @Override
    public List<String> getExtendById(int hospitalId, String type) {
        String sql = "SELECT `content` FROM hospital_extend WHERE hospital_id=:hospitalId AND type=:type";
        MapSqlParameterSource childParam = new MapSqlParameterSource()
                .addValue("hospitalId", hospitalId)
                .addValue("type", type);
        return db.query(sql, childParam, (ResultSet rs, int rowNum) -> rs.getString("content"));
    }
}
package com.sunchs.lyt.question.dao.ipml;

import com.sunchs.lyt.question.bean.QuestionAttributeData;
import com.sunchs.lyt.question.dao.QuestionAttributeDao;
import com.sunchs.lyt.question.exception.QuestionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Repository
public class QuestionAttributeDaoImpl implements QuestionAttributeDao {

    @Autowired
    private NamedParameterJdbcTemplate db;

    @Override
    public QuestionAttributeData getById(Integer id) {
        String sql = "SELECT `id`,`pid`,`title`,`status`,`remarks` FROM question_attribute WHERE `id`=:id";
        MapSqlParameterSource param = new MapSqlParameterSource()
                .addValue("id", id);
        QuestionAttributeData targetData =  selectForObject(sql, param, (ResultSet rs, int rowNum) -> setResultToData(rs));
        if (targetData != null) {
            String childSql = "SELECT `id`,`pid`,`title`,`status`,`remarks` FROM question_attribute WHERE `pid`=:id ORDER BY `sort` ASC";
            MapSqlParameterSource childParam = new MapSqlParameterSource()
                    .addValue("id", targetData.id);
            List<QuestionAttributeData> childList = db.query(childSql, childParam, (ResultSet rs, int rowNum) -> setResultToData(rs));
            targetData.setChildren(childList);
        }
        return targetData;
    }

    @Override
    public List<QuestionAttributeData> getList(Integer id) {
        String childSql = "SELECT `id` FROM question_attribute WHERE `pid`=:id ORDER BY `sort` ASC";
        MapSqlParameterSource childParam = new MapSqlParameterSource()
                .addValue("id", id);
        List<Integer> list = db.query(childSql, childParam, (ResultSet rs, int rowNum) -> rs.getInt("id"));
        List<QuestionAttributeData> result = new ArrayList<>();
        for (Integer tid : list) {
            result.add(getById(tid));
        }
        return result;
    }

    @Override
    public int getCount(Integer id) {
        String childSql = "SELECT COUNT(*) FROM question_attribute WHERE `pid`=:id";
        MapSqlParameterSource childParam = new MapSqlParameterSource()
                .addValue("id", id);
        Integer total = db.queryForObject(childSql, childParam, Integer.class);
        return total.intValue();
    }

    @Override
    public int insert(Map<String, Object> param) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = "INSERT INTO question_attribute(`pid`,`title`,`status`,`remarks`) VALUES(:pid, :title, 1, :remarks)";
        try {
            db.update(sql, new MapSqlParameterSource(param), keyHolder);
        } catch (Exception e) {
            throw new QuestionException("添加属性数据 --> 异常:" + e.getMessage());
        }
        return keyHolder.getKey().intValue();
    }

    @Override
    public int update(Map<String, Object> param) {
        return 0;
    }

    private QuestionAttributeData setResultToData(ResultSet rs) throws SQLException {
        QuestionAttributeData attributeData = new QuestionAttributeData();
        attributeData.setId(rs.getInt("id"));
        attributeData.setPid(rs.getInt("pid"));
        attributeData.setTitle(rs.getString("title"));
        attributeData.setStatus(rs.getInt("status"));
        attributeData.setRemarks(rs.getString("remarks"));
        return attributeData;
    }

    private <T> T selectForObject(String sql, MapSqlParameterSource param, RowMapper<T> mapper) {
        try {
            return db.queryForObject(sql, param, mapper);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

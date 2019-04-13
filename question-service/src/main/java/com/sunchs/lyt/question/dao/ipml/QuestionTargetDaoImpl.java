package com.sunchs.lyt.question.dao.ipml;

import com.sunchs.lyt.question.bean.QuestionTargetData;
import com.sunchs.lyt.question.dao.QuestionTargetDao;
import com.sunchs.lyt.question.exception.QuestionException;
import org.springframework.beans.factory.annotation.Autowired;
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

@Repository
public class QuestionTargetDaoImpl implements QuestionTargetDao {

    @Autowired
    private NamedParameterJdbcTemplate db;

    @Override
    public QuestionTargetData getById(Integer id) {
        String sql = "SELECT `id`,`pid`,`title`,`status`,`remarks` FROM question_target WHERE `id`=:id";
        MapSqlParameterSource param = new MapSqlParameterSource()
                .addValue("id", id);
        QuestionTargetData targetData = db.queryForObject(sql, param, (ResultSet rs, int rowNum) -> setResultToData(rs));
        if (targetData != null) {
            String childSql = "SELECT `id`,`pid`,`title`,`status`,`remarks` FROM question_target WHERE `pid`=:id ORDER BY `sort` ASC";
            MapSqlParameterSource childParam = new MapSqlParameterSource()
                    .addValue("id", targetData.id);
            List<QuestionTargetData> childList = db.query(childSql, childParam, (ResultSet rs, int rowNum) -> setResultToData(rs));
            targetData.setChildren(childList);
        }
        return targetData;
    }

    @Override
    public List<QuestionTargetData> getList(Integer id) {
        String childSql = "SELECT `id` FROM question_target WHERE `pid`=:id ORDER BY `sort` ASC";
        MapSqlParameterSource childParam = new MapSqlParameterSource()
                .addValue("id", id);
        List<Integer> list = db.query(childSql, childParam, (ResultSet rs, int rowNum) -> rs.getInt("id"));
        List<QuestionTargetData> result = new ArrayList<>();
        for (Integer tid : list) {
            result.add(getById(tid));
        }
        return result;
    }

    @Override
    public List<QuestionTargetData> getAll() {
        String sql = "SELECT `id`,`pid`,`title`,`status`,`remarks` FROM question_target ORDER BY `sort` ASC";
        return db.query(sql, (ResultSet rs, int rowNum) -> setResultToData(rs));
    }

    @Override
    public String getNameById(Integer id) {
        String sql = "SELECT `title` FROM question_target WHERE id=:id";
        MapSqlParameterSource param = new MapSqlParameterSource()
                .addValue("id", id);
        return selectForObject(sql, param, (ResultSet rs, int rowNum) -> rs.getString("title"));
    }

    @Override
    public int getCount(Integer id) {
        String childSql = "SELECT COUNT(*) FROM question_target WHERE `pid`=:id";
        MapSqlParameterSource childParam = new MapSqlParameterSource()
                .addValue("id", id);
        Integer total = db.queryForObject(childSql, childParam, Integer.class);
        return total.intValue();
    }

    @Override
    public int insert(Map<String, Object> param) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = "INSERT INTO question_target(`pid`,`title`,`status`,`remarks`) VALUES(:pid, :title, 1, :remarks)";
        try {
            db.update(sql, new MapSqlParameterSource(param), keyHolder);
        } catch (Exception e) {
            throw new QuestionException("添加指标数据 --> 异常:" + e.getMessage());
        }
        return keyHolder.getKey().intValue();
    }

    @Override
    public int update(Map<String, Object> param) {
        return 0;
    }

    public int titleQty(String title, int target) {
        String sql = "SELECT COUNT(*) total FROM question_target WHERE title=:title AND `pid`=:target";
        MapSqlParameterSource param = new MapSqlParameterSource()
                .addValue("target", target)
                .addValue("title", title);
        Integer total = db.queryForObject(sql, param, Integer.class);
        if (total.intValue() > 0) {
            return total.intValue();
        } else {
            String sql2 = "SELECT COUNT(*) total FROM question_target WHERE title=:title AND `pid` IN " +
                    "(SELECT id FROM question_target WHERE `pid`=:target AND title=:title)";
            MapSqlParameterSource source = new MapSqlParameterSource()
                    .addValue("target", target)
                    .addValue("title", title);
            Integer total2 = db.queryForObject(sql2, source, Integer.class);
            if (total2.intValue() > 0) {
                return total2.intValue();
            }
        }
        return 0;
    }

    private QuestionTargetData setResultToData(ResultSet rs) throws SQLException {
        QuestionTargetData targetData = new QuestionTargetData();
        targetData.setId(rs.getInt("id"));
        targetData.setPid(rs.getInt("pid"));
        targetData.setTitle(rs.getString("title"));
        targetData.setStatus(rs.getInt("status"));
        targetData.setRemarks(rs.getString("remarks"));
        return targetData;
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

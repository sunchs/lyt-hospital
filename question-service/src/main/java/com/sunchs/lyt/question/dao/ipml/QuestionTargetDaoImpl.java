package com.sunchs.lyt.question.dao.ipml;

import com.sunchs.lyt.question.bean.QuestionTargetData;
import com.sunchs.lyt.question.dao.QuestionTargetDao;
import com.sunchs.lyt.question.exception.QuestionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Repository
public class QuestionTargetDaoImpl implements QuestionTargetDao {

    @Autowired
    private NamedParameterJdbcTemplate db;

    @Override
    public QuestionTargetData getById(Integer id) {
        String sql = "SELECT `id`,`pid`,`title`,`status`,`sort` FROM question_target WHERE `id`=:id";
        MapSqlParameterSource param = new MapSqlParameterSource()
                .addValue("id", id);
        QuestionTargetData targetData =  db.query(sql, param, (ResultSet rs) -> {
            if ( ! rs.next()) {
                return null;
            }
            return setResultToData(rs);
        });
        if (targetData != null) {
            String childSql = "SELECT `id`,`pid`,`title`,`status`,`sort` FROM question_target WHERE `pid`=:id ORDER BY `sort` ASC";
            MapSqlParameterSource childParam = new MapSqlParameterSource()
                    .addValue("id", targetData.id);
            List<QuestionTargetData> childList = db.query(childSql, childParam, (ResultSet rs, int rowNum) -> setResultToData(rs));
            System.out.println(childList.size());
            targetData.setChildren(childList);
        }
        return targetData;
    }

    @Override
    public Integer insert(Map<String, Object> param) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = "INSERT INTO question_target(`pid`,`title`,`sort`,`status`) VALUES(:pid, :title, :sort, 1)";
        try {
            db.update(sql, new MapSqlParameterSource(param), keyHolder);
        } catch (Exception e) {
            throw new QuestionException("添加指标数据 --> 异常:" + e.getMessage());
        }
        return keyHolder.getKey().intValue();
    }

    @Override
    public Integer update(Map<String, Object> param) {
        return null;
    }

    private QuestionTargetData setResultToData(ResultSet rs) throws SQLException {
        QuestionTargetData targetData = new QuestionTargetData();
        targetData.setId(rs.getInt("id"));
        targetData.setPid(rs.getInt("pid"));
        targetData.setTitle(rs.getString("title"));
        targetData.setStatus(rs.getInt("status"));
        targetData.setSort(rs.getInt("sort"));
        return targetData;
    }
}

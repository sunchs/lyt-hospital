package com.sunchs.lyt.question.dao.ipml;

import com.sunchs.lyt.question.bean.QuestionData;
import com.sunchs.lyt.question.dao.QuestionDao;
import com.sunchs.lyt.question.exception.QuestionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class QuestionDaoImpl implements QuestionDao {

    @Autowired
    private NamedParameterJdbcTemplate db;

    @Override
    public QuestionData getById(Integer id) {
        return null;
    }

    @Override
    public Integer insert(Map<String, Object> param) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = "INSERT INTO question(`title`,`score`,`remark`,`update_id`,`update_time`,`create_id`,`create_time`," +
                "`target_one`,`target_two`,`target_three`,`option_type`) " +
                "VALUES(:title, :score, :remark, :updateId, :updateTime, :createId, :createTime, " +
                ":targetOne, :targetTwo, :targetThree, :optionType)";
        try {
            db.update(sql, new MapSqlParameterSource(param), keyHolder);
        } catch (Exception e) {
            throw new QuestionException("添加问题数据 --> 异常:" + e.getMessage());
        }
        return keyHolder.getKey().intValue();
    }

    @Override
    public Integer update(Map<String, Object> param) {
        return null;
    }

    @Override
    public void insertQuestionOption(Map<String, Object> param) {
        try {
            String sql = "INSERT INTO question_option(`question_id`,`title`,`score`,`sort`) " +
                    "VALUES(:questionId, :title, :score, :sort)";
            db.update(sql, new MapSqlParameterSource(param));
        } catch (Exception e) {
            throw new QuestionException("插入问题选项 --> 异常:" + e.getMessage());
        }
    }

    @Override
    public void deleteQuestionOption(Integer questionId) {
        try {
            String delSql = "DELETE FROM question_option WHERE question_id=:questionId";
            MapSqlParameterSource delParam = new MapSqlParameterSource()
                    .addValue("questionId", questionId);
            db.update(delSql, delParam);
        } catch (Exception e) {
            throw new QuestionException("删除问题选项 --> 异常:" + e.getMessage());
        }
    }
}

package com.sunchs.lyt.question.dao.ipml;

import com.sunchs.lyt.framework.util.PagingUtil;
import com.sunchs.lyt.question.bean.*;
import com.sunchs.lyt.question.dao.QuestionDao;
import com.sunchs.lyt.question.exception.QuestionException;
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
public class QuestionDaoImpl implements QuestionDao {

    @Autowired
    private NamedParameterJdbcTemplate db;

    @Autowired
    QuestionOptionDaoImpl questionOptionDao;

    @Override
    public QuestionData getById(Integer id) {
        String sql = "SELECT `id`,`title`,`status`,`score`,`remark`,`update_id`,`update_time`,`create_id`,`create_time`, " +
                "`target_one`,`target_two`,`target_three`,`option_type` FROM question WHERE id=:id";
        MapSqlParameterSource param = new MapSqlParameterSource()
                .addValue("id", id);
        QuestionData targetData =  db.query(sql, param, (ResultSet rs) -> {
            if (rs.next()) {
                QuestionData data = new QuestionData();
                data.setId(rs.getInt("id"));
                data.setTitle(rs.getString("title"));
                data.setScore(rs.getInt("score"));
                data.setStatus(rs.getInt("status"));
                data.setRemark(rs.getString("remark"));
                data.setOptionType(rs.getInt("option_type"));
                TargetData target = new TargetData();
                target.setOne(rs.getInt("target_one"));
                target.setTwo(rs.getInt("target_two"));
                target.setThree(rs.getInt("target_three"));
                data.setTarget(target);
                List<OptionData> optionDataList = questionOptionDao.getListById(data.getId());
                data.setOption(optionDataList);
                return data;
            }
            return null;
        });
        return targetData;
    }

    @Override
    public List<QuestionData> getPageList(QuestionParam param) {
        int skip = PagingUtil.getSkip(param.getPageNow(), param.getPageSize());
        String sql = "SELECT `id` FROM question ORDER BY `id` DESC LIMIT :skip,:pageSize";
        MapSqlParameterSource childParam = new MapSqlParameterSource()
                .addValue("skip", skip)
                .addValue("pageSize", param.getPageSize());
        List<Integer> ids = db.query(sql, childParam, (ResultSet rs, int rowNum) -> rs.getInt("id"));
        List<QuestionData> result = new ArrayList<>();
        for (int id : ids) {
            result.add(getById(id));
        }
        return result;
    }

    @Override
    public int getCount(QuestionParam param) {
        String sql = "SELECT COUNT(*) total FROM question";
        int total =  db.query(sql, (ResultSet rs) -> {
            if (rs.next()) {
                return rs.getInt("total");
            }
            return 0;
        });
        return total;
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
    public int update(Map<String, Object> param) {
        try {
            String sql = "UPDATE question SET `status`=:status WHERE id=:id";
            db.update(sql, new MapSqlParameterSource(param));
            return (int) param.get("id");
        } catch (Exception e) {
            throw new QuestionException("修改问题状态 --> 异常:" + e.getMessage());
        }
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

class a
{
    int id;// 问题ID
    int status;// 问题状态：1开启，2停用
}
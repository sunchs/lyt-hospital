package com.sunchs.lyt.question.dao.ipml;

import com.sunchs.lyt.framework.util.PagingUtil;
import com.sunchs.lyt.question.bean.QuestionnaireData;
import com.sunchs.lyt.question.bean.QuestionnaireParam;
import com.sunchs.lyt.question.dao.QuestionnaireDao;
import com.sunchs.lyt.question.exception.QuestionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

@Repository
public class QuestionnaireDaoImpl implements QuestionnaireDao {

    @Override
    public int getCount(QuestionnaireParam param) {
        String sql = "SELECT COUNT(*) FROM questionnaire";
        Integer integer = db.queryForObject(sql, new MapSqlParameterSource(), Integer.class);
        return integer.intValue();
    }

    @Autowired
    private NamedParameterJdbcTemplate db;

    @Override
    public List<QuestionnaireData> getPageList(QuestionnaireParam param) {
        int skip = PagingUtil.getSkip(param.getPageNow(), param.getPageSize());
        String sql = "SELECT `id`,`title`,`status`,`update_time` FROM questionnaire ORDER BY `id` DESC LIMIT :skip,:pageSize";
        MapSqlParameterSource childParam = new MapSqlParameterSource()
                .addValue("skip", skip)
                .addValue("pageSize", param.getPageSize());
        List<QuestionnaireData> list = db.query(sql, childParam, (ResultSet rs, int rowNum) -> {
            QuestionnaireData data = new QuestionnaireData();
            data.setId(rs.getInt("id"));
            data.setTitle(rs.getString("title"));
            data.setStatus(rs.getInt("status"));

            Timestamp updateTime = rs.getTimestamp("update_time");
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            data.setUpdateTime(dateFormat.format(updateTime));
            return data;
        });
        return list;
    }

    @Override
    public int insert(Map<String, Object> param) {
        try {
            GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
            String sql = "INSERT INTO questionnaire(`title`,`update_id`,`update_time`,`create_id`,`create_time`) " +
                    "VALUES(:title, :updateId, :updateTime, :createId, :createTime)";
            db.update(sql, new MapSqlParameterSource(param), keyHolder);
            return keyHolder.getKey().intValue();
        } catch (Exception e) {
            throw new QuestionException("添加问卷数据 --> 异常:" + e.getMessage());
        }
    }

    @Override
    public int update(Map<String, Object> param) {
        try {
            String sql = "UPDATE questionnaire SET `status`=:status,`update_id`=:updateId,`update_time`=:updateTime WHERE id=:id";
            db.update(sql, new MapSqlParameterSource(param));
            return (int) param.get("id");
        } catch (Exception e) {
            throw new QuestionException("修改问卷状态 --> 异常:" + e.getMessage());
        }
    }
}

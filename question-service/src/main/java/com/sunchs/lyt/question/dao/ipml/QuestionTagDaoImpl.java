package com.sunchs.lyt.question.dao.ipml;

import com.sunchs.lyt.question.bean.QuestionTagData;
import com.sunchs.lyt.question.bean.TagData;
import com.sunchs.lyt.question.dao.QuestionTagDao;
import com.sunchs.lyt.question.exception.QuestionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class QuestionTagDaoImpl implements QuestionTagDao {

    @Autowired
    private NamedParameterJdbcTemplate db;

    @Override
    public QuestionTagData getById(int id) {
        String sql = "SELECT `id`,`pid`,`title`,`status`,`remarks`,`update_time` FROM question_tag WHERE `id`=:id";
        MapSqlParameterSource param = new MapSqlParameterSource()
                .addValue("id", id);
        QuestionTagData targetData =  selectForObject(sql, param, (ResultSet rs, int rowNum) -> setResultToData(rs));
        if (targetData != null) {
            String childSql = "SELECT `id`,`pid`,`title`,`status`,`remarks`,`update_time` FROM question_tag WHERE `pid`=:id ORDER BY `id` DESC";
            MapSqlParameterSource childParam = new MapSqlParameterSource()
                    .addValue("id", targetData.id);
            List<QuestionTagData> childList = db.query(childSql, childParam, (ResultSet rs, int rowNum) -> setResultToData(rs));
            targetData.setChildren(childList);
        }
        return targetData;
    }

    @Override
    public List<QuestionTagData> getList(int id) {
        String childSql = "SELECT `id` FROM question_tag WHERE `pid`=:id AND `status`=1 ORDER BY `id` DESC";
        MapSqlParameterSource childParam = new MapSqlParameterSource()
                .addValue("id", id);
        List<Integer> list = db.query(childSql, childParam, (ResultSet rs, int rowNum) -> rs.getInt("id"));
        List<QuestionTagData> result = new ArrayList<>();
        for (Integer tid : list) {
            result.add(getById(tid));
        }
        return result;
    }

    @Override
    public int getCount(int id) {
        String childSql = "SELECT COUNT(*) FROM question_tag WHERE `pid`=:id AND `status`=1";
        MapSqlParameterSource childParam = new MapSqlParameterSource()
                .addValue("id", id);
        Integer total = db.queryForObject(childSql, childParam, Integer.class);
        return total.intValue();
    }

    @Override
    public int insert(Map<String, Object> param) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = "INSERT INTO question_tag(`pid`,`title`,`status`,`remarks`,`update_id`,`update_time`,`create_id`,`create_time`) " +
                "VALUES(:pid, :title, 1, :remarks, :updateId, :updateTime, :createId, :createTime)";
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

    @Override
    public List<TagData> getQuestionTag(int questionId) {
        String sql = "SELECT `tag_type`,`tag_id` FROM question_tag_binding WHERE `question_id`=:questionId";
        MapSqlParameterSource param = new MapSqlParameterSource()
                .addValue("questionId", questionId);
        List<TagData> list = db.query(sql, param, (ResultSet rs, int rowNum) -> {
            TagData tagData = new TagData();
            tagData.setType(rs.getInt("tag_type"));
            tagData.setTypeName(getNameById(tagData.getType()));
            tagData.setTagId(rs.getInt("tag_id"));
            tagData.setTagName(getNameById(tagData.getTagId()));
            return tagData;
        });
        return list;
    }

    @Override
    public String getNameById(int id) {
        try {
            String sql = "SELECT `title` FROM question_tag WHERE id=:id";
            MapSqlParameterSource param = new MapSqlParameterSource()
                    .addValue("id", id);
            return selectForObject(sql, param, (ResultSet rs, int rowNum) -> rs.getString("title"));
        } catch (Exception e) {
            return "";
        }
    }

    private QuestionTagData setResultToData(ResultSet rs) throws SQLException {
        QuestionTagData tagData = new QuestionTagData();
        tagData.setId(rs.getInt("id"));
        tagData.setPid(rs.getInt("pid"));
        tagData.setTitle(rs.getString("title"));
        tagData.setStatus(rs.getInt("status"));
        tagData.setRemarks(rs.getString("remarks"));

        Timestamp updateTime = rs.getTimestamp("update_time");
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        tagData.setUpdateTime(dateFormat.format(updateTime));
        return tagData;
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

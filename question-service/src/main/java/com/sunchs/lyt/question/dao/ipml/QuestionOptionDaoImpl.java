package com.sunchs.lyt.question.dao.ipml;

import com.sunchs.lyt.question.bean.OptionBean;
import com.sunchs.lyt.question.bean.OptionData;
import com.sunchs.lyt.question.bean.QuestionOptionData;
import com.sunchs.lyt.question.dao.QuestionOptionDao;
import com.sunchs.lyt.question.exception.QuestionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

@Repository
public class QuestionOptionDaoImpl implements QuestionOptionDao {

    @Autowired
    private NamedParameterJdbcTemplate db;

    @Override
    public List<OptionData> getListById(int questionId) {
        String childSql = "SELECT `id`,`title`,`sort` FROM question_option WHERE `question_id`=:questionId";
        MapSqlParameterSource childParam = new MapSqlParameterSource()
                .addValue("questionId", questionId);
        List<OptionData> list = db.query(childSql, childParam, (ResultSet rs, int rowNum) -> {
            OptionData option = new OptionData();
            option.setOptionId(rs.getInt("id"));
            option.setOptionName(rs.getString("title"));
            option.setSort(rs.getInt("sort"));
            return option;
        });
        return list;
    }

    @Override
    public QuestionOptionData getInfo(int id) {
        String sql = "SELECT `id`,`content`,`remarks`,`mode` FROM option_template WHERE `id`=:id";
        MapSqlParameterSource param = new MapSqlParameterSource()
                .addValue("id", id);
        QuestionOptionData info = db.queryForObject(sql, param, (ResultSet rs, int rowNum) -> {
            QuestionOptionData option = new QuestionOptionData();
            option.setId(rs.getInt("id"));
            option.setTitle(rs.getString("content"));
            option.setRemarks(rs.getString("remarks"));
            option.setMode(rs.getString("mode"));
            return option;
        });
        return info;
    }

    @Override
    public List<OptionBean> getOptionList(int id) {
        String sql = "SELECT `id`,`content` FROM option_template WHERE `pid`=:pid";
        MapSqlParameterSource param = new MapSqlParameterSource()
                .addValue("pid", id);
        return db.query(sql, param, (ResultSet rs, int rowNum) -> {
            OptionBean optionBean = new OptionBean();
            optionBean.setOptionId(rs.getInt("id"));
            optionBean.setOptionContent(rs.getString("content"));
            return optionBean;
        });
    }

    @Override
    public List<QuestionOptionData> getTypeList() {
        String sql = "SELECT `id`,`content`,`remarks` FROM option_template WHERE `pid`=0 ORDER BY `sort` ASC";
        return db.query(sql, new MapSqlParameterSource(), (ResultSet rs, int rowNum) -> {
            QuestionOptionData option = new QuestionOptionData();
            option.setId(rs.getInt("id"));
            option.setTitle(rs.getString("content"));
            option.setRemarks(rs.getString("remarks"));
            return option;
        });
    }

    @Override
    public OptionBean getOption(int id) {
        try {
            String sql = "SELECT `id`,`content` FROM option_template WHERE `id`=:id";
            MapSqlParameterSource param = new MapSqlParameterSource()
                    .addValue("id", id);
            OptionBean info = db.queryForObject(sql, param, (ResultSet rs, int rowNum) -> {
                OptionBean option = new OptionBean();
                option.setOptionId(rs.getInt("id"));
                option.setOptionContent(rs.getString("content"));
                return option;
            });
            return info;
        } catch (Exception e) {
            throw new QuestionException("获取选项数据 --> 异常:" + id + " --> " + e.getMessage());
        }
    }

    @Override
    public int update(Map<String, Object> param) {
        try {
            String sql = "UPDATE option_template SET `remarks`=:remarks WHERE id=:id";
            db.update(sql, new MapSqlParameterSource(param));
            return (int) param.get("id");
        } catch (Exception e) {
            throw new QuestionException("更新选项备注 --> 异常:" + e.getMessage());
        }
    }

    @Override
    public void insertOption(int typeId, String content) {
        try {
            String sql = "INSERT INTO option_template(`pid`,`content`) VALUES(:typeId, :content)";
            MapSqlParameterSource param = new MapSqlParameterSource()
                    .addValue("typeId", typeId)
                    .addValue("content", content);
            db.update(sql, param);
        } catch (Exception e) {
            throw new QuestionException("插入选项内容 --> 异常:" + e.getMessage());
        }
    }

    @Override
    public void deleteOption(int id) {
        try {
            String sql = "DELETE FROM option_template WHERE pid=:id";
            MapSqlParameterSource param = new MapSqlParameterSource()
                    .addValue("id", id);
            db.update(sql, param);
        } catch (Exception e) {
            throw new QuestionException("删除选项内容 --> 异常:" + e.getMessage());
        }
    }
}
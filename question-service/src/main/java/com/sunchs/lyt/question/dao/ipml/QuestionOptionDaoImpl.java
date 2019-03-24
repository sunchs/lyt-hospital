package com.sunchs.lyt.question.dao.ipml;

import com.sunchs.lyt.question.bean.OptionData;
import com.sunchs.lyt.question.dao.QuestionOptionDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.util.List;

@Repository
public class QuestionOptionDaoImpl implements QuestionOptionDao {

    @Autowired
    private NamedParameterJdbcTemplate db;

    @Override
    public List<OptionData> getListById(Integer questionId) {
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
}

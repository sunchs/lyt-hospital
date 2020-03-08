package com.sunchs.lyt.question.dao.ipml;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.sunchs.lyt.db.business.entity.QuestionTarget;
import com.sunchs.lyt.db.business.service.impl.QuestionTargetServiceImpl;
import com.sunchs.lyt.framework.util.JsonUtil;
import com.sunchs.lyt.framework.util.ObjectUtil;
import com.sunchs.lyt.question.bean.QuestionTargetData;
import com.sunchs.lyt.question.dao.QuestionTargetDao;
import com.sunchs.lyt.question.exception.QuestionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository
public class QuestionTargetDaoImpl implements QuestionTargetDao {

    @Autowired
    private NamedParameterJdbcTemplate db;

    @Autowired
    private QuestionTargetServiceImpl questionTargetService;

    @Override
    public QuestionTargetData getById(int id) {
        Wrapper<QuestionTarget> where = new EntityWrapper<>();
        where.eq("id", id);
        QuestionTarget questionTarget = questionTargetService.selectOne(where);
        if (Objects.nonNull(questionTarget)) {
            // 转换对象
            String oldString = JsonUtil.toJson(questionTarget);
            QuestionTargetData data = JsonUtil.toObject(oldString, QuestionTargetData.class);

            // 获取子集合
            Wrapper<QuestionTarget> child = new EntityWrapper<>();
            child.eq("pid", id);
            List<QuestionTarget> list = questionTargetService.selectList(child);
            List<QuestionTargetData> listData = new ArrayList<>();
            for (QuestionTarget target : list) {
                QuestionTargetData targetData = ObjectUtil.copy(target, QuestionTargetData.class);
                targetData.initData();
                listData.add(targetData);
            }
            data.setChildren(listData);
            return data;
        }
        return null;
    }

//    @Override
//    public List<QuestionTargetData> getList(int id) {
//        String childSql = "SELECT `id` FROM question_target WHERE `pid`=:id ORDER BY `sort` ASC";
//        MapSqlParameterSource childParam = new MapSqlParameterSource()
//                .addValue("id", id);
//        List<Integer> list = db.query(childSql, childParam, (ResultSet rs, int rowNum) -> rs.getInt("id"));
//        List<QuestionTargetData> result = new ArrayList<>();
//        for (Integer tid : list) {
//            result.add(getById(tid));
//        }
//        return result;
//    }

    @Override
    public List<QuestionTarget> getAll() {
        Wrapper<QuestionTarget> where = new EntityWrapper<QuestionTarget>()
                .eq(QuestionTarget.STATUS, 1);
        return questionTargetService.selectList(where);
    }

    @Override
    public String getNameById(int id) {
        try {
            String sql = "SELECT `title` FROM question_target WHERE id=:id";
            MapSqlParameterSource param = new MapSqlParameterSource()
                    .addValue("id", id);
            return db.queryForObject(sql, param, (ResultSet rs, int rowNum) -> rs.getString("title"));
        } catch (Exception e) {
            return "";
        }
    }
//
//    @Override
//    public int getCount(int id) {
//        String childSql = "SELECT COUNT(*) FROM question_target WHERE `pid`=:id";
//        MapSqlParameterSource childParam = new MapSqlParameterSource()
//                .addValue("id", id);
//        Integer total = db.queryForObject(childSql, childParam, Integer.class);
//        return total.intValue();
//    }

    @Override
    public boolean insert(QuestionTarget questionTarget) {
        try {
            return questionTargetService.insert(questionTarget);
        } catch (Exception e) {
            throw new QuestionException("添加指标数据 --> 异常:" + e.getMessage());
        }
    }

//    @Override
//    public int update(Map<String, Object> param) {
//        return 0;
//    }

    @Override
    public int titleQty(String title, int target) {
        String sql = "SELECT COUNT(*) total FROM question_target WHERE title=:title AND `pid`=:target AND `status`<>2";
        MapSqlParameterSource param = new MapSqlParameterSource()
                .addValue("target", target)
                .addValue("title", title);
        Integer total = db.queryForObject(sql, param, Integer.class);
        if (total.intValue() > 0) {
            return total.intValue();
        } else {
            String sql2 = "SELECT COUNT(*) total FROM question_target WHERE title=:title AND `pid` IN " +
                    "(SELECT id FROM question_target WHERE `pid`=:target AND `status`<>2)";
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

//    private QuestionTargetData setResultToData(ResultSet rs) throws SQLException {
//        QuestionTargetData targetData = new QuestionTargetData();
//        targetData.setId(rs.getInt("id"));
//        targetData.setPid(rs.getInt("pid"));
//        targetData.setTitle(rs.getString("title"));
//        targetData.setStatus(rs.getInt("status"));
//        targetData.setRemarks(rs.getString("remarks"));
//
////        Timestamp updateTime = rs.getTimestamp("update_time");
////        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
////        targetData.setUpdateTime(dateFormat.format(updateTime));
//        return targetData;
//    }
}

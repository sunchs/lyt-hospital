package com.sunchs.lyt.user.dao.ipml;

import com.sunchs.lyt.user.bean.UserData;
import com.sunchs.lyt.user.dao.UserDao;
import com.sunchs.lyt.user.exception.UserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class UserDaoImpl implements UserDao {

    @Autowired
    private NamedParameterJdbcTemplate db;

    @Override
    public UserData getUserByPassword(String username, String password) {


        return null;
    }

    @Override
    public UserData getUserByToken(String token) {
        return null;
    }

    @Override
    public Integer addUserData(Map<String, Object> params) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = "INSERT INTO user(username,password,name,create_time,pw_log) " +
                "VALUES(:userName, :passWord, :name, :createTime, :pwLog)";
        try {
            db.update(sql, new MapSqlParameterSource(params), keyHolder);
        } catch (Exception e) {
            e.printStackTrace();
            throw new UserException("添加用户数据，SQL异常");
        }
        return keyHolder.getKey().intValue();
    }
}

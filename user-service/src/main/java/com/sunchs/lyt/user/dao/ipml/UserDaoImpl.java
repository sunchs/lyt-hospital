package com.sunchs.lyt.user.dao.ipml;

import com.sunchs.lyt.user.bean.UserData;
import com.sunchs.lyt.user.dao.UserDao;
import com.sunchs.lyt.user.exception.UserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.util.Map;

@Repository
public class UserDaoImpl implements UserDao {

    @Autowired
    private NamedParameterJdbcTemplate db;

    @Override
    public UserData getUserByAccount(String userName, String passWord) {
        String sql = "SELECT user_id,username,name,token FROM user WHERE username=:userName AND password=:passWord";
        MapSqlParameterSource param = new MapSqlParameterSource()
                .addValue("userName", userName)
                .addValue("passWord", passWord);
        UserData user = db.query(sql, param, (ResultSet rs) -> {
            if ( ! rs.next()) {
                return null;
            }
            UserData u = new UserData();
            u.setUserId(rs.getInt("user_id"));
            u.setUserName(rs.getString("username"));
            u.setName(rs.getString("name"));
            u.setToken(rs.getString("token"));
            return u;
        });
        return user;
    }

    @Override
    public UserData getUserByToken(String token) {
        return null;
    }

    @Override
    public UserData getUserById(Integer userId) {
        String sql = "SELECT user_id,username,name,token FROM user WHERE user_id=:userId";
        MapSqlParameterSource param = new MapSqlParameterSource()
                .addValue("userId", userId);
        UserData user = db.query(sql, param, (ResultSet rs) -> {
            if ( ! rs.next()) {
                return null;
            }
            UserData u = new UserData();
            u.setUserId(rs.getInt("user_id"));
            u.setName(rs.getString("name"));
            u.setUserName(rs.getString("username"));
            u.setToken(rs.getString("token"));
            return u;
        });
        return user;
    }

    @Override
    public Integer addUser(Map<String, Object> params) {
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

    @Override
    public boolean updateUser(Map<String, Object> params) {
        StringBuffer sql = new StringBuffer();
        sql.append("UPDATE user SET create_time=create_time");
        if (params.containsKey("passWord")) {
            sql.append(", password=:passWord");
        }
        if (params.containsKey("pwLog")) {
            sql.append(", pw_log=:pwLog");
        }
        if (params.containsKey("name")) {
            sql.append(", name=:name");
        }
        if (params.containsKey("token")) {
            sql.append(", token=:token");
        }
        sql.append(" WHERE user_id=:userId");

        try {
            db.update(sql.toString(), new MapSqlParameterSource(params));
        } catch (Exception e) {
            throw new UserException("修改用户数据，SQL异常");
        }
        return true;
    }

    @Override
    public void saveUserRole(Integer userId, Integer roleId) {
        String delSql = "DELETE FROM user_role WHERE user_id=:userId";
        String istSql = "INSERT INTO user_role(user_id,role_id) VALUES(:userId, :roleId)";
        MapSqlParameterSource param = new MapSqlParameterSource()
                .addValue("userId", userId)
                .addValue("roleId", roleId);
        db.update(delSql, param);
        db.update(istSql, param);
    }
}

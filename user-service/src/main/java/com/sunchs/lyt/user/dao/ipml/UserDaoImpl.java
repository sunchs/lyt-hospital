package com.sunchs.lyt.user.dao.ipml;

import com.sunchs.lyt.user.bean.UserRoleData;
import com.sunchs.lyt.user.dao.UserDao;
import com.sunchs.lyt.user.exception.UserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

@Repository
public class UserDaoImpl implements UserDao {

    @Autowired
    private NamedParameterJdbcTemplate db;

    @Override
    public UserRoleData getUserByAccount(String userName, String passWord) {
        String sql = "SELECT user_id,username,name,token FROM user WHERE username=:userName AND password=:passWord";
        MapSqlParameterSource param = new MapSqlParameterSource()
                .addValue("userName", userName)
                .addValue("passWord", passWord);
        UserRoleData user = db.query(sql, param, (ResultSet rs) -> {
            if ( ! rs.next()) {
                return null;
            }
            UserRoleData u = new UserRoleData();
            u.setUserId(rs.getInt("user_id"));
            u.setUserName(rs.getString("username"));
            u.setName(rs.getString("name"));
            u.setToken(rs.getString("token"));
            return u;
        });
        return user;
    }

    @Override
    public UserRoleData getUserByToken(String token) {
        return null;
    }

    @Override
    public UserRoleData getUserById(Integer userId) {
        String sql = "SELECT user_id,username,name,token FROM user WHERE user_id=:userId";
        MapSqlParameterSource param = new MapSqlParameterSource()
                .addValue("userId", userId);
        UserRoleData user = db.query(sql, param, (ResultSet rs) -> {
            if ( ! rs.next()) {
                return null;
            }
            UserRoleData u = new UserRoleData();
            u.setUserId(rs.getInt("user_id"));
            u.setName(rs.getString("name"));
            u.setUserName(rs.getString("username"));
            u.setToken(rs.getString("token"));
            return u;
        });
        return user;
    }

    @Override
    public Integer insertUser(Map<String, Object> param) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = "INSERT INTO user(username,password,name,create_time,pw_log) " +
                "VALUES(:userName, :passWord, :name, :createTime, :pwLog)";
        try {
            db.update(sql, new MapSqlParameterSource(param), keyHolder);
        } catch (Exception e) {
            throw new UserException("添加用户数据，SQL异常");
        }
        return keyHolder.getKey().intValue();
    }

    @Override
    public Integer updateUser(Map<String, Object> param) {
        StringBuffer sql = new StringBuffer();
        sql.append("UPDATE user SET create_time=create_time");
        if (param.containsKey("passWord")) {
            sql.append(", password=:passWord");
        }
        if (param.containsKey("pwLog")) {
            sql.append(", pw_log=:pwLog");
        }
        if (param.containsKey("name")) {
            sql.append(", name=:name");
        }
        if (param.containsKey("token")) {
            sql.append(", token=:token");
        }
        sql.append(" WHERE user_id=:userId");
        try {
            db.update(sql.toString(), new MapSqlParameterSource(param));
            if (param.containsKey("userId")) {
                return (int) param.get("userId");
            }
        } catch (Exception e) {
            throw new UserException("修改用户数据，SQL异常");
        }
        return 0;
    }

    @Override
    public void saveUserRole(Integer userId, List<Integer> roleList) {
        String delSql = "DELETE FROM user_role WHERE user_id=:userId";
        MapSqlParameterSource delParam = new MapSqlParameterSource()
                .addValue("userId", userId);
        db.update(delSql, delParam);

        String istSql = "INSERT INTO user_role(user_id,role_id) VALUES(:userId, :roleId)";
        roleList.forEach(roleId->{
            MapSqlParameterSource param = new MapSqlParameterSource()
                    .addValue("userId", userId)
                    .addValue("roleId", roleId);
            db.update(istSql, param);
        });
    }

    @Override
    public boolean isExistUserName(String userName) {
        String sql = "SELECT user_id FROM user WHERE username=:userName";
        MapSqlParameterSource param = new MapSqlParameterSource()
                .addValue("userName", userName);
        return db.query(sql, param, (ResultSet rs) -> rs.next());
    }
}

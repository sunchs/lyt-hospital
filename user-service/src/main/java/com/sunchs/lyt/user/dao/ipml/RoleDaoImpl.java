package com.sunchs.lyt.user.dao.ipml;

import com.sunchs.lyt.user.bean.RoleData;
import com.sunchs.lyt.user.dao.RoleDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class RoleDaoImpl implements RoleDao {

    @Autowired
    private NamedParameterJdbcTemplate db;

    @Override
    public RoleData getRoleById(Integer roleId) {
        String sql = "SELECT role_id,title FROM role WHERE role_id=:roleId";
        MapSqlParameterSource param = new MapSqlParameterSource()
                .addValue("roleId", roleId);
        RoleData role = db.query(sql, param, (ResultSet rs) -> {
            if ( ! rs.next()) {
                return null;
            }
            RoleData r = new RoleData();
            r.setRoleId(rs.getInt("role_id"));
            r.setTitle(rs.getString("title"));
            return r;
        });
        return role;
    }

    @Override
    public RoleData getRoleByUserId(Integer userId) {
        String sql = "SELECT role_id,title FROM role WHERE role_id=(SELECT role_id FROM user_role WHERE user_id=:userId) LIMIT 1";
        MapSqlParameterSource param = new MapSqlParameterSource()
                .addValue("userId", userId);
        RoleData role = db.query(sql, param, (ResultSet rs) -> {
            if ( ! rs.next()) {
                return null;
            }
            RoleData r = new RoleData();
            r.setRoleId(rs.getInt("role_id"));
            r.setTitle(rs.getString("title"));
            return r;
        });
        return role;
    }

    @Override
    public List<RoleData> getRoleList() {
        String sql = "SELECT role_id,title FROM role WHERE enabled=1 ORDER BY sort ASC";
        List<RoleData> list = db.query(sql, (ResultSet rs, int rowNum) -> {
            RoleData r = new RoleData();
            r.setRoleId(rs.getInt("role_id"));
            r.setTitle(rs.getString("title"));
            return r;
        });
        return list;
    }
}
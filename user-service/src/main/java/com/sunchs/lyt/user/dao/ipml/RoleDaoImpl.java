package com.sunchs.lyt.user.dao.ipml;

import com.sunchs.lyt.user.bean.RoleData;
import com.sunchs.lyt.user.dao.RoleDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;

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
}

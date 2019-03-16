package com.sunchs.lyt.user.dao.ipml;

import com.sunchs.lyt.user.bean.RoleNodeData;
import com.sunchs.lyt.user.bean.RoleData;
import com.sunchs.lyt.user.dao.RoleDao;
import com.sunchs.lyt.user.exception.UserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Repository
public class RoleDaoImpl implements RoleDao {

    @Autowired
    private NamedParameterJdbcTemplate db;

    @Override
    public RoleNodeData getRoleById(Integer roleId) {
        String sql = "SELECT role_id,title FROM role WHERE role_id=:roleId";
        MapSqlParameterSource param = new MapSqlParameterSource()
                .addValue("roleId", roleId);
        RoleNodeData role = db.query(sql, param, (ResultSet rs) -> {
            if ( ! rs.next()) {
                return null;
            }
            RoleNodeData r = new RoleNodeData();
            r.setRoleId(rs.getInt("role_id"));
            r.setTitle(rs.getString("title"));
            return r;
        });
        return role;
    }

    @Override
    public List<RoleData> getRoleByUserId(Integer userId) {
        String sql = "SELECT role_id,title FROM role WHERE role_id IN (SELECT role_id FROM user_role WHERE user_id=:userId)";
        MapSqlParameterSource param = new MapSqlParameterSource()
                .addValue("userId", userId);
        return db.query(sql, param, (ResultSet rs, int rowNum) -> setResultToRoleData(rs));
    }

    @Override
    public List<RoleData> getRoleList() {
        String sql = "SELECT role_id,title FROM role WHERE enabled=1 ORDER BY sort ASC";
        return  db.query(sql, (ResultSet rs, int rowNum) -> setResultToRoleData(rs));
    }

    @Override
    public Integer insertRoleData(Map<String,Object> param) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = "INSERT INTO role(title) VALUES(:title)";
        try {
            db.update(sql, new MapSqlParameterSource(param), keyHolder);
        } catch (Exception e) {
            e.printStackTrace();
            throw new UserException("添加角色数据，SQL异常");
        }
        return keyHolder.getKey().intValue();
    }

    @Override
    public Integer updateRoleData(Map<String, Object> param) {
        String sql = "UPDATE role SET title=:title WHERE role_id=:roleId";
        try {
            db.update(sql, new MapSqlParameterSource(param));
            if (param.containsKey("roleId")) {
                return (int) param.get("roleId");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new UserException("修改角色数据，SQL异常");
        }
        return 0;
    }

    @Override
    public void deleteRoleNode(Integer roleId) {
        String sql = "DELETE FROM role_node WHERE role_id=:roleId";
        MapSqlParameterSource param = new MapSqlParameterSource()
                .addValue("roleId", roleId);
        db.update(sql, param);
    }

    @Override
    public void addRoleNode(Integer roleId, Integer nodeId, Integer action) {
        String sql = "INSERT INTO role_node(role_id,node_id,action) VALUES(:roleId,:nodeId,:action)";
        MapSqlParameterSource param = new MapSqlParameterSource()
                .addValue("roleId", roleId)
                .addValue("nodeId", nodeId)
                .addValue("action", action);
        db.update(sql, param);
    }

    @Override
    public boolean isExistTitle(Integer roleId, String title) {
        String sql = "SELECT role_id FROM role WHERE title=:title AND role_id<>:roleId";
        MapSqlParameterSource param = new MapSqlParameterSource()
                .addValue("roleId", roleId)
                .addValue("title", title);
        return db.query(sql, param, (ResultSet rs) -> rs.next());
    }

    private RoleData setResultToRoleData(ResultSet rs) throws SQLException {
        RoleData r = new RoleData();
        r.setRoleId(rs.getInt("role_id"));
        r.setTitle(rs.getString("title"));
        return r;
    }
}
package com.sunchs.lyt.user.dao.ipml;

import com.sunchs.lyt.user.bean.NodeData;
import com.sunchs.lyt.user.bean.RoleData;
import com.sunchs.lyt.user.dao.NodeDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class NodeDaoImpl implements NodeDao {

    @Autowired
    private NamedParameterJdbcTemplate db;

    @Override
    public List<NodeData> getList() {
        String sql = "SELECT node_id,title FROM node WHERE enabled=1 ORDER BY sort ASC";
        List<NodeData> list = db.query(sql, (ResultSet rs, int rowNum) -> setResultToNodeData(rs));
        return list;
    }

    @Override
    public List<NodeData> getNodeByRoleId(Integer roleId) {
        String sql = "SELECT node_id,title FROM node WHERE node_id IN (SELECT node_id FROM role_node WHERE role_id=:roleId)";
        MapSqlParameterSource param = new MapSqlParameterSource()
                .addValue("roleId", roleId);
        return db.query(sql, param, (ResultSet rs, int rowNum) -> setResultToNodeData(rs));
    }

    private NodeData setResultToNodeData(ResultSet rs) throws SQLException {
        NodeData node = new NodeData();
        node.setNodeId(rs.getInt("node_id"));
        node.setTitle(rs.getString("title"));
        return node;
    }
}

package com.sunchs.lyt.user.dao.ipml;

import com.sunchs.lyt.user.bean.NodeData;
import com.sunchs.lyt.user.dao.NodeDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.util.List;

@Repository
public class NodeDaoImpl implements NodeDao {

    @Autowired
    private NamedParameterJdbcTemplate db;

    @Override
    public List<NodeData> getList() {
        String sql = "SELECT node_id,title FROM node WHERE enabled=1 ORDER BY sort ASC";
        List<NodeData> list = db.query(sql, (ResultSet rs, int rowNum) -> {
            NodeData n = new NodeData();
            n.setNodeId(rs.getInt("node_id"));
            n.setTitle(rs.getString("title"));
            return n;
        });
        return list;
    }

}

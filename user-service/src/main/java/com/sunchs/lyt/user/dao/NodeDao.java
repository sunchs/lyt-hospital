package com.sunchs.lyt.user.dao;

import com.sunchs.lyt.user.bean.NodeData;

import java.util.List;

public interface NodeDao {

    /**
     * 获取 角色 列表数据
     */
    List<NodeData> getList();
}

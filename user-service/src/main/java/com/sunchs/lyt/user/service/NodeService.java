package com.sunchs.lyt.user.service;

import com.sunchs.lyt.user.bean.NodeData;

import java.util.List;

public interface NodeService {

    /**
     * 获取 角色 列表数据
     */
    List<NodeData> getList();
}

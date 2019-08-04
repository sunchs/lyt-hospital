package com.sunchs.lyt.user.service;

import com.sunchs.lyt.user.bean.MenuData;
import com.sunchs.lyt.user.bean.NodeData;

import java.util.List;

public interface INodeService {

    /**
     * 获取 角色 列表数据
     */
    List<NodeData> getList();

    /**
     * 获取菜单列表
     */
    List<MenuData> getMenuList();
}

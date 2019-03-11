package com.sunchs.lyt.user.dao;

import com.sunchs.lyt.user.bean.RoleData;

import java.util.List;

public interface RoleDao {

    /**
     * 根据 角色ID 获取角色信息
     */
    RoleData getRoleById(Integer roleId);

    /**
     * 获取 角色 列表数据
     */
    List<RoleData> getRoleList();

}

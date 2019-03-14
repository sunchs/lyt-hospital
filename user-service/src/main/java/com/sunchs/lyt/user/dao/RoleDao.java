package com.sunchs.lyt.user.dao;

import com.sunchs.lyt.user.bean.RoleData;

import java.util.List;
import java.util.Map;

public interface RoleDao {

    /**
     * 根据 角色ID 获取角色信息
     */
    RoleData getRoleById(Integer roleId);

    /**
     * 根据 用户ID 获取角色信息
     */
    RoleData getRoleByUserId(Integer userId);

    /**
     * 获取 角色 列表数据
     */
    List<RoleData> getRoleList();

    /**
     * 插入 角色 数据
     */
    Integer insertRoleData(Map<String, Object> map);

    /**
     * 更新 角色 数据
     */
    boolean updateRoleData(Map<String, Object> map);

    /**
     * 删除角色绑定的节点
     */
    void deleteRoleNode(Integer roleId);

    /**
     * 角色 绑定 节点
     */
    void addRoleNode(Integer roleId, Integer nodeId, Integer action);

}

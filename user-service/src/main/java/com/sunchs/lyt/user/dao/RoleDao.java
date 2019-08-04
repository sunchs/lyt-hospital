package com.sunchs.lyt.user.dao;

public interface RoleDao {

//    /**
//     * 根据 角色ID 获取角色信息
//     */
//    RoleNodeData getRoleById(Integer roleId);
//
//    /**
//     * 根据 用户ID 获取角色信息
//     */
//    List<RoleData> getRoleByUserId(Integer userId);
//
//    /**
//     * 获取 角色 列表数据
//     */
//    List<RoleData> getRoleList();
//
//    /**
//     * 插入 角色 数据
//     */
//    Integer insertRoleData(Map<String, Object> param);
//
//    /**
//     * 更新 角色 数据
//     */
//    Integer updateRoleData(Map<String, Object> param);

    /**
     * 删除角色绑定的节点
     */
    void deleteRoleNode(Integer roleId);

    /**
     * 角色 绑定 节点
     */
    void addRoleNode(Integer roleId, Integer nodeId, Integer action);

    /**
     * 判断 角色 是否被使用
     */
    boolean isExistTitle(int roleId, String title);

}

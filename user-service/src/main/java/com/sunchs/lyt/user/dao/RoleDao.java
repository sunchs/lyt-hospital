package com.sunchs.lyt.user.dao;

import com.sunchs.lyt.user.bean.RoleData;

public interface RoleDao {

    /**
     * 根据 角色ID 获取角色信息
     */
    RoleData getRoleById(Integer roleId);

}

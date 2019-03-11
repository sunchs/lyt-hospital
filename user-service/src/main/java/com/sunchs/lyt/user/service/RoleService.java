package com.sunchs.lyt.user.service;

import com.sunchs.lyt.user.bean.RoleData;

import java.util.List;

public interface RoleService {

    /**
     * 获取 角色 列表数据
     */
    List<RoleData> getRoleList();
}

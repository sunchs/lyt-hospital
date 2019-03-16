package com.sunchs.lyt.user.service;

import com.sunchs.lyt.user.bean.RoleNodeData;
import com.sunchs.lyt.user.bean.RoleData;
import com.sunchs.lyt.user.bean.RoleParam;

import java.util.List;

public interface RoleService {

    /**
     * 获取 角色 列表数据
     */
    List<RoleData> getRoleList();

    /**
     * 添加、修改账号
     */
    RoleNodeData save(RoleParam param);
}

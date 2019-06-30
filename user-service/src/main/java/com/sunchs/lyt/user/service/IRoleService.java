package com.sunchs.lyt.user.service;

import com.sunchs.lyt.user.bean.RoleNodeData;
import com.sunchs.lyt.user.bean.RoleData;
import com.sunchs.lyt.user.bean.RoleParam;

import java.util.List;
import java.util.Map;

public interface IRoleService {

    /**
     * 获取 角色 列表数据
     */
    List<RoleData> getRoleList();

    /**
     * 添加、修改账号
     */
    RoleNodeData save(RoleParam param);

    /**
     * 用户绑定角色
     */
    void bindUserRole(int userId, List<Integer> roleList);

    /**
     * 获取select数据
     */
    List<Map<String, Object>> getSelectData();
}

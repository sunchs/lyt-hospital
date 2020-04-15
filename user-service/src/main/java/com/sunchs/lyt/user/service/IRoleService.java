package com.sunchs.lyt.user.service;

import com.sunchs.lyt.framework.bean.PagingList;
import com.sunchs.lyt.user.bean.RoleNodeData;
import com.sunchs.lyt.user.bean.RoleData;
import com.sunchs.lyt.user.bean.RoleParam;
import com.sunchs.lyt.user.bean.UserParam;

import java.util.List;
import java.util.Map;

public interface IRoleService {

    /**
     * 获取 角色 列表数据
     */
    PagingList<RoleData> getRoleList(RoleParam param);

    /**
     * 添加、修改账号
     */
    int save(RoleParam param);

    /**
     * 用户绑定角色
     */
    void bindUserRole(int userId, UserParam param);

    /**
     * 获取select数据
     */
    List<Map<String, Object>> getSelectData();
}

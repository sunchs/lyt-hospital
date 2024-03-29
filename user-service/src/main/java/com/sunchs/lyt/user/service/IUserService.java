package com.sunchs.lyt.user.service;

import com.sunchs.lyt.framework.bean.PagingList;
import com.sunchs.lyt.user.bean.UserData;
import com.sunchs.lyt.user.bean.UserParam;
import com.sunchs.lyt.user.bean.UserRoleData;

import java.util.List;
import java.util.Map;

public interface IUserService {

    /**
     * 添加、修改账号
     */
    int save(UserParam data);

    /**
     * 用户列表
     */
    PagingList<UserData> getPagingList(UserParam param);

    /**
     * 登录
     */
    UserRoleData login(UserParam param);

    /**
     * 退出
     */
    boolean logout();

    /**
     * 用户绑定医院
     */
    void bindUserHospital(int userId, UserParam param);

    /**
     * 获取可用的用户列表
     */
    List<Map<String, Object>> getUsableList();
}

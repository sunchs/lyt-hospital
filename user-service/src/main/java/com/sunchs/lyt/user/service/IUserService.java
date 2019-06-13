package com.sunchs.lyt.user.service;

import com.sunchs.lyt.user.bean.UserRoleData;
import com.sunchs.lyt.user.bean.UserParam;

public interface IUserService {

    /**
     * 登录
     */
    UserRoleData login(UserParam param);

    /**
     * 退出
     */
    boolean logout();

    /**
     * 添加、修改账号
     */
    int save(UserParam data);
}

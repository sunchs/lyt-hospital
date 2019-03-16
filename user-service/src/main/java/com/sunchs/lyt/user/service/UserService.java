package com.sunchs.lyt.user.service;

import com.sunchs.lyt.user.bean.UserRoleData;
import com.sunchs.lyt.user.bean.UserParam;

public interface UserService {

    /**
     * 登录
     */
    UserRoleData login(UserParam param);

    /**
     * 退出
     */
    boolean logout();

    /**
     * Token验证
     */
    void token(String token);

    /**
     * 添加、修改账号
     */
    UserRoleData save(UserParam data);
}

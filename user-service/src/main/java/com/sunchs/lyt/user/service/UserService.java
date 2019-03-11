package com.sunchs.lyt.user.service;

import com.sunchs.lyt.user.bean.UserData;
import com.sunchs.lyt.user.bean.UserParam;

public interface UserService {

    /**
     * 登录
     */
    UserData login(String username, String password);

    /**
     * Token验证
     */
    void token(String token);

    /**
     * 添加、修改账号
     */
    UserData saveAccount(UserParam data);
}

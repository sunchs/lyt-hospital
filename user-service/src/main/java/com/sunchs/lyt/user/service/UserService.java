package com.sunchs.lyt.user.service;

import com.sunchs.lyt.user.bean.UserData;

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
     * 添加账号
     */
    void addAccount(UserData data) throws RuntimeException;
}

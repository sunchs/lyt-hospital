package com.sunchs.lyt.user.dao;

import com.sunchs.lyt.user.bean.UserData;

import java.util.Map;

public interface UserDao {

    /**
     * 根据 账号密码 获取用户信息
     */
    UserData getUserByPassword(String username, String password);

    /**
     * 根据 Token 获取用户信息
     */
    UserData getUserByToken(String token);

    /**
     * 添加账号数据
     */
    Integer addUserData(Map<String, Object> params);
}

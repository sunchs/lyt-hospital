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
     * 根据 用户ID 获取用户信息
     */
    UserData getUserById(Integer userId);

    /**
     * 添加用户
     */
    Integer addUser(Map<String, Object> params);

    /**
     * 添加用户角色
     */
    void addUserRole(Integer userId, Integer roleId);
}

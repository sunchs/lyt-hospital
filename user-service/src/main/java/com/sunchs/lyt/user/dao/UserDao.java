package com.sunchs.lyt.user.dao;

import com.sunchs.lyt.user.bean.UserRoleData;

import java.util.List;
import java.util.Map;

public interface UserDao {

//    /**
//     * 根据 账号密码 获取用户信息
//     */
//    UserRoleData getUserByAccount(String username, String password);

    /**
     * 根据 Token 获取用户信息
     */
    UserRoleData getUserByToken(String token);
//
//    /**
//     * 根据 用户ID 获取用户信息
//     */
//    UserRoleData getUserById(Integer userId);

    /**
     * 添加用户
     */
    Integer insertUser(Map<String, Object> params);

    /**
     * 更新用户
     */
    Integer updateUser(Map<String, Object> params);

    /**
     * 添加用户角色
     */
    void saveUserRole(Integer userId, List<Integer> roleList);

    /**
     * 判断 用户名 是否被使用
     */
    boolean isExistUserName(String userName);
}

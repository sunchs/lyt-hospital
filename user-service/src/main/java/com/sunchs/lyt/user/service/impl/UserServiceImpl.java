package com.sunchs.lyt.user.service.impl;

import com.sunchs.lyt.framework.util.*;
import com.sunchs.lyt.user.bean.RoleData;
import com.sunchs.lyt.user.bean.UserData;
import com.sunchs.lyt.user.bean.UserParam;
import com.sunchs.lyt.user.dao.ipml.RoleDaoImpl;
import com.sunchs.lyt.user.dao.ipml.UserDaoImpl;
import com.sunchs.lyt.user.exception.UserException;
import com.sunchs.lyt.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserDaoImpl userDao;

    @Autowired
    RoleDaoImpl roleDao;

    @Override
    public UserData login(UserParam data) {
        this.checkAccount(data);
        UserData user = userDao.getUserByAccount(data.getUserName(), MD5Util.encode(data.getPassWord()));
        if (user == null) {
            throw new UserException("账号或者密码错误！");
        }
        String key = user.getUserName() + System.currentTimeMillis();
        String token = MD5Util.encode(key) + "-" + user.getUserId();
        Map<String, Object> param = new HashMap<>();
        param.put("userId", user.getUserId());
        param.put("token", token);
        if (userDao.updateUser(param)) {
            user = userDao.getUserById(user.getUserId());
            user.setRole(roleDao.getRoleByUserId(user.getUserId()));
        }
        // 缓存
        RedisUtil.setValue(token, JsonUtil.toJson(user));

        return user;
    }

    @Override
    public boolean logout() {
        try {
            RedisUtil.remove(UserUtil.getUserToken());
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public void token(String token) {

    }

    @Override
    public UserData saveAccount(UserParam data) {
        this.checkAccount(data);
        if (StringUtil.isEmpty(data.getName())) {
            throw new UserException("姓名不能为空");
        }
        if (NumberUtil.isZero(data.getRoleId())) {
            throw new UserException("角色不能为空");
        }
        Map<String, Object> param = new HashMap<>();
        param.put("userId", data.getUserId());
        param.put("userName", data.getUserName());
        param.put("passWord", MD5Util.encode(data.getPassWord()));
        param.put("name", data.getName());
        param.put("createTime", new Timestamp(System.currentTimeMillis()));
        param.put("pwLog", data.getPassWord());

        Integer userId = data.getUserId();
        if (NumberUtil.isZero(userId)) {
            userId = userDao.addUser(param);// 添加
        } else {
            userDao.updateUser(param);// 修改
        }
        if (userId > 0) {
            userDao.saveUserRole(userId, data.getRoleId());
            UserData user = userDao.getUserById(userId);
            RoleData role = roleDao.getRoleById(data.getRoleId());
            user.setRole(role);
            return user;
        }
        return null;
    }

    private void checkAccount(UserParam data) {
        if (StringUtil.isEmpty(data.getUserName())) {
            throw new UserException("用户名不能为空");
        }
        if (StringUtil.isEmpty(data.getPassWord())) {
            throw new UserException("密码不能为空");
        }
    }
}




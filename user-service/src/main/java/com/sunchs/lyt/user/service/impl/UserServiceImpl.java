package com.sunchs.lyt.user.service.impl;

import com.sunchs.lyt.framework.util.*;
import com.sunchs.lyt.user.bean.UserRoleData;
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
    public UserRoleData login(UserParam param) {
        this.checkUserName(param, false);
        this.checkPassWord(param);
        UserRoleData user = userDao.getUserByAccount(param.getUserName(), MD5Util.encode(param.getPassWord()));
        if (user == null) {
            throw new UserException("账号或者密码错误！");
        }
        Integer userId = user.getUserId();
        String token = MD5Util.encode(user.getUserName() + System.currentTimeMillis()) + "id" + userId;
        Map<String, Object> opt = new HashMap<>();
        opt.put("userId", userId);
        opt.put("token", token);
        if (userDao.updateUser(opt) > 0) {
            user = userDao.getUserById(userId);
            user.setRoleList(roleDao.getRoleByUserId(userId));
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
    public UserRoleData save(UserParam param) {
        Integer userId = 0;
        if (NumberUtil.isZero(param.getUserId())) {
            userId = this.insert(param);
        } else {
            userId = this.update(param);
        }
        if (userId > 0) {
            UserRoleData user = userDao.getUserById(userId);
            if (user == null) {
                throw new UserException("用户ID：" + userId + "，不存在");
            } else {
                user.setRoleList(roleDao.getRoleByUserId(userId));
            }
            return user;
        }
        return null;
    }

    private Integer insert(UserParam param) {
        this.checkUserName(param, true);
        this.checkPassWord(param);
        this.checkName(param);
        this.checkRole(param);

        Map<String, Object> opt = new HashMap<>();
        opt.put("userId", param.getUserId());
        opt.put("userName", param.getUserName());
        opt.put("passWord", MD5Util.encode(param.getPassWord()));
        opt.put("name", param.getName());
        opt.put("createTime", new Timestamp(System.currentTimeMillis()));
        opt.put("pwLog", param.getPassWord());
        Integer userId = userDao.insertUser(opt);
        if (userId > 0) {
            userDao.saveUserRole(userId, param.getRole());
        }
        return userId;
    }

    private Integer update(UserParam param) {
        Map<String, Object> opt = new HashMap<>();
        opt.put("userId", param.getUserId());
        if (StringUtil.isNotEmpty(param.getPassWord())) {
            opt.put("passWord", MD5Util.encode(param.getPassWord()));
            opt.put("pwLog", param.getPassWord());
        }
        if (StringUtil.isNotEmpty(param.getName())) {
            opt.put("name", param.getName());
        }
        Integer userId = userDao.updateUser(opt);
        if (userId > 0) {
            userDao.saveUserRole(userId, param.getRole());
        }
        return userId;
    }

    private void checkUserName(UserParam param, boolean isExist) {
        if (StringUtil.isEmpty(param.getUserName())) {
            throw new UserException("用户名不能为空");
        }
        if (isExist && userDao.isExistUserName(param.getUserName())) {
            throw new UserException("用户名已存在");
        }
    }

    private void checkPassWord(UserParam param) {
        if (StringUtil.isEmpty(param.getPassWord())) {
            throw new UserException("密码不能为空");
        }
    }

    private void checkName(UserParam param) {
        if (StringUtil.isEmpty(param.getName())) {
            throw new UserException("姓名不能为空");
        }
    }

    private void checkRole(UserParam param) {
        if (param.getRole() == null || param.getRole().size() == 0) {
            throw new UserException("角色不能为空");
        }
    }
}
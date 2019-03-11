package com.sunchs.lyt.user.service.impl;

import com.sunchs.lyt.framework.util.MD5Util;
import com.sunchs.lyt.framework.util.NumberUtil;
import com.sunchs.lyt.framework.util.StringUtil;
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
    public UserData login(String username, String password) {

        return null;
    }

    @Override
    public void token(String token) {

    }

    @Override
    public UserData addAccount(UserParam data) throws UserException {
        if (StringUtil.isEmpty(data.getUserName())) {
            throw new UserException("用户名不能为空");
        }
        if (StringUtil.isEmpty(data.getPassWord())) {
            throw new UserException("密码不能为空");
        }
        if (StringUtil.isEmpty(data.getName())) {
            throw new UserException("姓名不能为空");
        }
        if (NumberUtil.isZero(data.getRoleId())) {
            throw new UserException("角色不能为空");
        }
        Map<String, Object> param = new HashMap<>();
        param.put("userName", data.getUserName());
        param.put("passWord", MD5Util.encode(data.getPassWord()));
        param.put("name", data.getName());
        param.put("createTime", new Timestamp(System.currentTimeMillis()));
        param.put("pwLog", data.getPassWord());
        Integer userId = userDao.addUser(param);
        if (userId > 0) {
            userDao.addUserRole(userId, data.getRoleId());
            UserData user = userDao.getUserById(userId);
            RoleData role = roleDao.getRoleById(data.getRoleId());
            user.setRole(role);
            return user;
        }
        return null;
    }

}




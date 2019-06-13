package com.sunchs.lyt.user.bean;

import com.sunchs.lyt.framework.util.StringUtil;
import com.sunchs.lyt.user.exception.UserException;

import java.util.List;

public class UserParam {

    public Integer id;
    public String userName;
    public String passWord;
    public String name;
    public List<Integer> roleList;

    public void checkUserName() {
        if (StringUtil.isEmpty(userName)) {
            throw new UserException("用户名不能为空");
        }
    }

    public void checkPassWord() {
        if (StringUtil.isEmpty(passWord)) {
            throw new UserException("密码不能为空");
        }
    }

    public void checkName() {
        if (StringUtil.isEmpty(name)) {
            throw new UserException("姓名不能为空");
        }
    }

    public void checkRole() {
        if (roleList == null || roleList.size() == 0) {
            throw new UserException("角色不能为空");
        }
    }

    public Integer getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public String getName() {
        return name;
    }

    public List<Integer> getRoleList() {
        return roleList;
    }
}
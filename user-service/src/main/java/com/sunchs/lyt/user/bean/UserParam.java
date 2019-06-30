package com.sunchs.lyt.user.bean;

import com.sunchs.lyt.framework.bean.PagingParam;
import com.sunchs.lyt.framework.util.StringUtil;
import com.sunchs.lyt.user.exception.UserException;

import java.util.List;

public class UserParam extends PagingParam {

    private int id;
    private String userName;
    private String passWord;
    private String name;
    private int status;
    private List<Integer> roleList;

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

    public int getId() {
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

    public int getStatus() {
        return status;
    }

    public List<Integer> getRoleList() {
        return roleList;
    }
}
package com.sunchs.lyt.user.bean;

import java.util.List;

public class UserParam {

    public Integer userId;
    public String userName;
    public String passWord;
    public String name;
    public List<Integer> role;

    public Integer getUserId() {
        return userId;
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

    public List<Integer> getRole() {
        return role;
    }
}
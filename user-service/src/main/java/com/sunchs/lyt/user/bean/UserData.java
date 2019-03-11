package com.sunchs.lyt.user.bean;

public class UserData {

    public Integer userId;
    public String userName;
    public String name;
    public String token;
    public RoleData role;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public RoleData getRole() {
        return role;
    }

    public void setRole(RoleData role) {
        this.role = role;
    }
}

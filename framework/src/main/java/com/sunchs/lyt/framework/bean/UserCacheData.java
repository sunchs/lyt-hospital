package com.sunchs.lyt.framework.bean;

public class UserCacheData {

    private int id;
    private String userName;
    private String name;
    private String token;

    public int getUserId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public String getName() {
        return name;
    }

    public String getToken() {
        return token;
    }

    @Override
    public String toString() {
        return "UserCacheData{" +
                "userId=" + id +
                ", userName='" + userName + '\'' +
                ", name='" + name + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}



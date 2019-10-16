package com.sunchs.lyt.user.bean;

import java.util.Set;

public class UserRoleData {

    private int id;
    private int type;
    private String userName;
    private String name;
    private String token;
    private int hospitalId;
    private Set<Integer> roleList;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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

    public int getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(int hospitalId) {
        this.hospitalId = hospitalId;
    }

    public Set<Integer> getRoleList() {
        return roleList;
    }

    public void setRoleList(Set<Integer> roleList) {
        this.roleList = roleList;
    }
}

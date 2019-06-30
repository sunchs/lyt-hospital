package com.sunchs.lyt.user.bean;

import com.sunchs.lyt.db.business.entity.User;

public class UserData extends User {

    private String statusName;

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }
}

package com.sunchs.lyt.hospital.bean;

import com.sunchs.lyt.db.business.entity.Member;

public class MemberData extends Member {

    private int authCode;

    public int getAuthCode() {
        return authCode;
    }

    public void setAuthCode(int authCode) {
        this.authCode = authCode;
    }
}

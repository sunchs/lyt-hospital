package com.sunchs.lyt.hospital.bean;

public class MemberParam {

    private int id;

    /**
     * 手机号码
     */
    private String phone;

    /**
     * 姓名
     */
    private String name;

    /**
     * 身份证
     */
    private String identityCard;

    /**
     * 验证码
     */
    private String authCode;


    public int getId() {
        return id;
    }

    public String getPhone() {
        return phone;
    }

    public String getName() {
        return name;
    }

    public String getIdentityCard() {
        return identityCard;
    }

    public String getAuthCode() {
        return authCode;
    }
}

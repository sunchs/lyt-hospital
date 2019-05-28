package com.sunchs.lyt.hospital.enums;

public enum ExtendTypeEnum {

    /**
     * 分院信息
     */
    Branch(1, "分院信息"),

    /**
     * 门诊类别
     */
    OutpatientType(2, "门诊类别"),

    /**
     * 挂号方式
     */
    RegistrationMode(4, "挂号方式");

    public int value;
    public String title;

    ExtendTypeEnum(int value, String title) {
        this.value = value;
        this.title = title;
    }

}

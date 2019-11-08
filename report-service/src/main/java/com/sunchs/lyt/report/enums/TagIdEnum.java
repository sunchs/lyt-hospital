package com.sunchs.lyt.report.enums;

public enum TagIdEnum {

    /**
     * 性别
     */
    Sex(2, "性别"),

    /**
     * 年龄
     */
    Age(3, "年龄"),

    /**
     * 来院方式
     */
    Come(4, "来院方式"),

    /**
     * 居住地
     */
    Address(4, "居住地");

    public int status;
    public String title;

    TagIdEnum(int status, String title) {
        this.status = status;
        this.title = title;
    }

    public static String get(int status) {
        for (TagIdEnum statusEnum : TagIdEnum.values()) {
            if (status == statusEnum.status) {
                return statusEnum.title;
            }
        }
        return "";
    }
}

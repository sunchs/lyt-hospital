package com.sunchs.lyt.item.enums;

public enum  AnswerStatusEnum {

    /**
     * 待审核
     */
    WaitCheck(0, "待审核"),

    /**
     * 已审核
     */
    Checked(1, "已审核"),

    /**
     * 审核不通过
     */
    NoPass(2, "审核不通过");

    private int status;
    private String title;

    AnswerStatusEnum(int status, String title) {
        this.status = status;
        this.title = title;
    }

    public static String get(int status) {
        for (AnswerStatusEnum statusEnum : AnswerStatusEnum.values()) {
            if (status == statusEnum.status) {
                return statusEnum.title;
            }
        }
        return "";
    }

}

package com.sunchs.lyt.question.enums;

public enum QuestionStatusEnum {

    /**
     * 开启
     */
    Start(1, "已启用"),

    /**
     * 停用
     */
    Stop(0, "已停用"),

    /**
     * 删除
     */
    Delete(100, "删除");

    public int status;
    public String title;

    QuestionStatusEnum(int status, String title) {
        this.status = status;
        this.title = title;
    }

    public static String get(int status) {
        for (QuestionStatusEnum statusEnum : QuestionStatusEnum.values()) {
            if (status == statusEnum.status) {
                return statusEnum.title;
            }
        }
        return "";
    }
}

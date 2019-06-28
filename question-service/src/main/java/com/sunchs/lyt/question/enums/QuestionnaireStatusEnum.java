package com.sunchs.lyt.question.enums;

public enum QuestionnaireStatusEnum {

    /**
     * 草稿
     */
    Draft(0, "草稿"),

    /**
     * 发布
     */
    Release(1, "发布"),

    /**
     * 删除
     */
    Delete(100, "删除");

    public int status;
    public String title;

    QuestionnaireStatusEnum(int status, String title) {
        this.status = status;
        this.title = title;
    }

    public static String get(int status) {
        for (QuestionnaireStatusEnum statusEnum : QuestionnaireStatusEnum.values()) {
            if (status == statusEnum.status) {
                return statusEnum.title;
            }
        }
        return "";
    }

}

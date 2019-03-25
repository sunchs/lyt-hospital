package com.sunchs.lyt.question.enums;

public enum QuestionStatus {

    Start(1, "已启用"),
    Stop(0, "已停用");

    public int status;// 状态ID
    public String title;// 状态名称

    QuestionStatus(int status, String title) {
        this.status = status;
        this.title = title;
    }

    public static String getName(int status) {
        if (status == 1) {
            return Start.title;
        } else if (status == 0) {
            return Stop.title;
        }
        return "";
    }
}

package com.sunchs.lyt.user.enums;

public enum StatusEnum {

    /**
     * 停用
     */
    Stop(0, "停用"),

    /**
     * 启用
     */
    Start(1, "启用"),

    /**
     * 删除
     */
    Delete(2, "删除");

    private int status;
    private String name;

    StatusEnum(int status, String name) {
        this.setStatus(status);
        this.setName(name);
    }

    public static String getName(int status) {
        switch (status) {
            case 0:
                return Stop.getName();
            case 1:
                return Start.getName();
            case 2:
                return Delete.getName();
            default:
                return "无状态";
        }
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

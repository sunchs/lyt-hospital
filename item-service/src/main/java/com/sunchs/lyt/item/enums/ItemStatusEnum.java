package com.sunchs.lyt.item.enums;

public enum ItemStatusEnum {

    /**
     * 开启
     */
    Start(1, "启用"),

    /**
     * 停用
     */
    Stop(0, "停用"),

    /**
     * 删除
     */
    Delete(100, "删除");

    public int status;
    public String title;

    ItemStatusEnum(int status, String title) {
        this.status = status;
        this.title = title;
    }

    public static String get(int status) {
        for (ItemStatusEnum statusEnum : ItemStatusEnum.values()) {
            if (status == statusEnum.status) {
                return statusEnum.title;
            }
        }
        return "";
    }
}

package com.sunchs.lyt.hospital.bean;

public class HospitalOfficeParam {

    /**
     * ID
     */
    private int id;

    /**
     * 科室类型
     */
    private int type;

    /**
     * 组名
     */
    private String groupName;

    /**
     * 科室名称
     */
    private String name;

    /**
     * 年数量
     */
    private int quantity;

    public int getId() {
        return id;
    }

    public int getType() {
        return type;
    }

    public String getGroupName() {
        return groupName;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }
}

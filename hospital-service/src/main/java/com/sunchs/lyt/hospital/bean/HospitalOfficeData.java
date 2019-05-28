package com.sunchs.lyt.hospital.bean;

public class HospitalOfficeData {

    /**
     * ID
     */
    private int id;

    /**
     * 科室名称
     */
    private String name;

    /**
     * 年数量
     */
    private int yearQuantity;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getYearQuantity() {
        return yearQuantity;
    }

    public void setYearQuantity(int yearQuantity) {
        this.yearQuantity = yearQuantity;
    }
}

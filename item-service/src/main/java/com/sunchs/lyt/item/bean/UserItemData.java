package com.sunchs.lyt.item.bean;

import java.util.List;

public class UserItemData {

    private int itemId;
    private String ItemName;
    private String number;
    private int hospitalId;
    private String hospitalName;
    private List<UserItemOfficeData> officeList;

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(int hospitalId) {
        this.hospitalId = hospitalId;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public List<UserItemOfficeData> getOfficeList() {
        return officeList;
    }

    public void setOfficeList(List<UserItemOfficeData> officeList) {
        this.officeList = officeList;
    }
}

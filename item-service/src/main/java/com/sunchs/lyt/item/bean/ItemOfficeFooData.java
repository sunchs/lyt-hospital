package com.sunchs.lyt.item.bean;

import com.sunchs.lyt.db.business.entity.ItemOffice;

public class ItemOfficeFooData extends ItemOffice {

    /**
     * 医院名称
     */
    private String hospitalName;

    /**
     * 科室类型
     */
    private String officeTypeName;

    /**
     * 科室名称
     */
    private String officeName;

    /**
     * 问卷名称
     */
    private String questionnaireName;

//    /**
//     * 题目列表
//     */
//    private List<>


    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public String getOfficeTypeName() {
        return officeTypeName;
    }

    public void setOfficeTypeName(String officeTypeName) {
        this.officeTypeName = officeTypeName;
    }

    public String getOfficeName() {
        return officeName;
    }

    public void setOfficeName(String officeName) {
        this.officeName = officeName;
    }

    public String getQuestionnaireName() {
        return questionnaireName;
    }

    public void setQuestionnaireName(String questionnaireName) {
        this.questionnaireName = questionnaireName;
    }
}

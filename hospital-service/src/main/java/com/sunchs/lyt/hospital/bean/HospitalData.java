package com.sunchs.lyt.hospital.bean;

import com.sunchs.lyt.db.business.entity.Hospital;

import java.util.List;

public class HospitalData extends Hospital {

    /**
     * 地区
     */
    private List<Integer> region;

    /**
     * 门诊科室
     */
    private List<HospitalOfficeData> outpatientOffice;

    /**
     * 住院科室
     */
    private List<HospitalOfficeData> inpatientOffice;

    /**
     * 特殊科室
     */
    private List<HospitalOfficeData> specialOffice;

    /**
     * 分院信息
     */
    private List<HospitalExtendData> hospitalBranch;

    /**
     * 门诊类别
     */
    private List<HospitalExtendData> outpatientType;

    /**
     * 挂号方式
     */
    private List<HospitalExtendData> registrationMode;


    private String statusName;

    private String hospitalTypeName;

    private String hospitalPropertyName;

    private String subjectionName;

    private String updateTimeName;


    public List<Integer> getRegion() {
        return region;
    }

    public void setRegion(List<Integer> region) {
        this.region = region;
    }

    public List<HospitalOfficeData> getOutpatientOffice() {
        return outpatientOffice;
    }

    public void setOutpatientOffice(List<HospitalOfficeData> outpatientOffice) {
        this.outpatientOffice = outpatientOffice;
    }

    public List<HospitalOfficeData> getInpatientOffice() {
        return inpatientOffice;
    }

    public void setInpatientOffice(List<HospitalOfficeData> inpatientOffice) {
        this.inpatientOffice = inpatientOffice;
    }

    public List<HospitalOfficeData> getSpecialOffice() {
        return specialOffice;
    }

    public void setSpecialOffice(List<HospitalOfficeData> specialOffice) {
        this.specialOffice = specialOffice;
    }

    public List<HospitalExtendData> getHospitalBranch() {
        return hospitalBranch;
    }

    public void setHospitalBranch(List<HospitalExtendData> hospitalBranch) {
        this.hospitalBranch = hospitalBranch;
    }

    public List<HospitalExtendData> getOutpatientType() {
        return outpatientType;
    }

    public void setOutpatientType(List<HospitalExtendData> outpatientType) {
        this.outpatientType = outpatientType;
    }

    public List<HospitalExtendData> getRegistrationMode() {
        return registrationMode;
    }

    public void setRegistrationMode(List<HospitalExtendData> registrationMode) {
        this.registrationMode = registrationMode;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getHospitalTypeName() {
        return hospitalTypeName;
    }

    public void setHospitalTypeName(String hospitalTypeName) {
        this.hospitalTypeName = hospitalTypeName;
    }

    public String getHospitalPropertyName() {
        return hospitalPropertyName;
    }

    public void setHospitalPropertyName(String hospitalPropertyName) {
        this.hospitalPropertyName = hospitalPropertyName;
    }

    public String getSubjectionName() {
        return subjectionName;
    }

    public void setSubjectionName(String subjectionName) {
        this.subjectionName = subjectionName;
    }

    public String getUpdateTimeName() {
        return updateTimeName;
    }

    public void setUpdateTimeName(String updateTimeName) {
        this.updateTimeName = updateTimeName;
    }
}
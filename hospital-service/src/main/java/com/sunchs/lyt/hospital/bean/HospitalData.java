package com.sunchs.lyt.hospital.bean;

import java.util.List;

public class HospitalData {

    /**
     * 医院ID
     */
    public int id;

    /**
     * 医院名称
     */
    public String hospitalName;

    /**
     * 医院类型
     */
    public int hospitalType;

    /**
     * 医院性质
     */
    public int hospitalProperty;

    /**
     * 隶属
     */
    public int subjection;

    /**
     * 地址
     */
    public String address;

    /**
     * 联系人
     */
    public String contacts;

    /**
     * 联系方式
     */
    public String contactInfo;

    /**
     * 业务领导
     */
    public String operationName;

    /**
     * 业务电话
     */
    public String operationPhone;

    /**
     * 开放床位
     */
    public int openBeds;

    /**
     * 备注
     */
    public String remarks;

    /**
     * 门诊科室
     */
    public List<String> outpatientOffice;

    /**
     * 住院科室
     */
    public List<String> inpatientOffice;

    /**
     * 特殊科室
     */
    public List<String> specialOffice;

    /**
     * 分院信息
     */
    public List<HospitalBranchParam> hospitalBranch;

    /**
     * 门诊类别
     */
    public List<String> outpatientType;

    /**
     * 挂号方式
     */
    public List<String> registrationMode;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public int getHospitalType() {
        return hospitalType;
    }

    public void setHospitalType(int hospitalType) {
        this.hospitalType = hospitalType;
    }

    public int getHospitalProperty() {
        return hospitalProperty;
    }

    public void setHospitalProperty(int hospitalProperty) {
        this.hospitalProperty = hospitalProperty;
    }

    public int getSubjection() {
        return subjection;
    }

    public void setSubjection(int subjection) {
        this.subjection = subjection;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContacts() {
        return contacts;
    }

    public void setContacts(String contacts) {
        this.contacts = contacts;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public String getOperationName() {
        return operationName;
    }

    public void setOperationName(String operationName) {
        this.operationName = operationName;
    }

    public String getOperationPhone() {
        return operationPhone;
    }

    public void setOperationPhone(String operationPhone) {
        this.operationPhone = operationPhone;
    }

    public int getOpenBeds() {
        return openBeds;
    }

    public void setOpenBeds(int openBeds) {
        this.openBeds = openBeds;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public List<String> getOutpatientOffice() {
        return outpatientOffice;
    }

    public void setOutpatientOffice(List<String> outpatientOffice) {
        this.outpatientOffice = outpatientOffice;
    }

    public List<String> getInpatientOffice() {
        return inpatientOffice;
    }

    public void setInpatientOffice(List<String> inpatientOffice) {
        this.inpatientOffice = inpatientOffice;
    }

    public List<String> getSpecialOffice() {
        return specialOffice;
    }

    public void setSpecialOffice(List<String> specialOffice) {
        this.specialOffice = specialOffice;
    }

    public List<HospitalBranchParam> getHospitalBranch() {
        return hospitalBranch;
    }

    public void setHospitalBranch(List<HospitalBranchParam> hospitalBranch) {
        this.hospitalBranch = hospitalBranch;
    }

    public List<String> getOutpatientType() {
        return outpatientType;
    }

    public void setOutpatientType(List<String> outpatientType) {
        this.outpatientType = outpatientType;
    }

    public List<String> getRegistrationMode() {
        return registrationMode;
    }

    public void setRegistrationMode(List<String> registrationMode) {
        this.registrationMode = registrationMode;
    }
}

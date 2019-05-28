package com.sunchs.lyt.hospital.bean;

import com.sunchs.lyt.db.business.entity.Hospital;

import java.util.List;

public class HospitalData extends Hospital {

    /**
     * 地区
     */
    public List<Integer> region;

    /**
     * 门诊科室
     */
    public List<HospitalOfficeData> outpatientOffice;

    /**
     * 住院科室
     */
    public List<HospitalOfficeData> inpatientOffice;

    /**
     * 特殊科室
     */
    public List<HospitalOfficeData> specialOffice;

    /**
     * 分院信息
     */
    public List<HospitalExtendData> hospitalBranch;

    /**
     * 门诊类别
     */
    public List<HospitalExtendData> outpatientType;

    /**
     * 挂号方式
     */
    public List<HospitalExtendData> registrationMode;


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
}
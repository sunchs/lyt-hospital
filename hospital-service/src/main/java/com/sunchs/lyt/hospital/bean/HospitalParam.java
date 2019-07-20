package com.sunchs.lyt.hospital.bean;

import com.sunchs.lyt.framework.bean.PagingParam;

import java.util.List;

/**
 * 医院信息参数
 * @author king
 */
public class HospitalParam extends PagingParam {

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
     * 地区集合
     */
    public List<Integer> region;

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
    public List<HospitalOfficeParam> outpatientOffice;

    /**
     * 住院科室
     */
    public List<HospitalOfficeParam> inpatientOffice;

    /**
     * 特殊科室
     */
    public List<HospitalOfficeParam> specialOffice;

    /**
     * 分院信息
     */
    public List<HospitalExtendParam> hospitalBranch;

    /**
     * 门诊类别
     */
    public List<HospitalExtendParam> outpatientType;

    /**
     * 挂号方式
     */
    public List<HospitalExtendParam> registrationMode;


    public int getId() {
        return id;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public int getHospitalType() {
        return hospitalType;
    }

    public int getHospitalProperty() {
        return hospitalProperty;
    }

    public int getSubjection() {
        return subjection;
    }

    public List<Integer> getRegion() {
        return region;
    }

    public String getAddress() {
        return address;
    }

    public String getContacts() {
        return contacts;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public String getOperationName() {
        return operationName;
    }

    public String getOperationPhone() {
        return operationPhone;
    }

    public int getOpenBeds() {
        return openBeds;
    }

    public String getRemarks() {
        return remarks;
    }

    public List<HospitalOfficeParam> getOutpatientOffice() {
        return outpatientOffice;
    }

    public List<HospitalOfficeParam> getInpatientOffice() {
        return inpatientOffice;
    }

    public List<HospitalOfficeParam> getSpecialOffice() {
        return specialOffice;
    }

    public List<HospitalExtendParam> getHospitalBranch() {
        return hospitalBranch;
    }

    public List<HospitalExtendParam> getOutpatientType() {
        return outpatientType;
    }

    public List<HospitalExtendParam> getRegistrationMode() {
        return registrationMode;
    }

    @Override
    public String toString() {
        return "HospitalParam{" +
                "id=" + id +
                ", hospitalName='" + hospitalName + '\'' +
                ", hospitalType=" + hospitalType +
                ", hospitalProperty=" + hospitalProperty +
                ", subjection=" + subjection +
                ", region=" + region +
                ", address='" + address + '\'' +
                ", contacts='" + contacts + '\'' +
                ", contactInfo='" + contactInfo + '\'' +
                ", operationName='" + operationName + '\'' +
                ", operationPhone='" + operationPhone + '\'' +
                ", openBeds=" + openBeds +
                ", remarks='" + remarks + '\'' +
                ", outpatientOffice=" + outpatientOffice +
                ", inpatientOffice=" + inpatientOffice +
                ", specialOffice=" + specialOffice +
                ", hospitalBranch=" + hospitalBranch +
                ", outpatientType=" + outpatientType +
                ", registrationMode=" + registrationMode +
                '}';
    }
}
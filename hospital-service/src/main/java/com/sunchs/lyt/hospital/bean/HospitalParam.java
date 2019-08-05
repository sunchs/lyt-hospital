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
    private int id;

    /**
     * 医院名称
     */
    private String hospitalName;

    /**
     * 是否开启
     */
    private int status;

    /**
     * 医院类型
     */
    private int hospitalType;

    /**
     * 医院性质
     */
    private int hospitalProperty;

    /**
     * 隶属
     */
    private int subjection;

    /**
     * 地区集合
     */
    private List<Integer> region;

    /**
     * 地址
     */
    private String address;

    /**
     * 联系人
     */
    private String contacts;

    /**
     * 联系方式
     */
    private String contactInfo;

    /**
     * 业务领导
     */
    private String operationName;

    /**
     * 业务电话
     */
    private String operationPhone;

    /**
     * 开放床位
     */
    private int openBeds;

    /**
     * 备注
     */
    private String remarks;

    /**
     * 门诊科室
     */
    private List<HospitalOfficeParam> outpatientOffice;

    /**
     * 住院科室
     */
    private List<HospitalOfficeParam> inpatientOffice;

    /**
     * 特殊科室
     */
    private List<HospitalOfficeParam> specialOffice;

    /**
     * 分院信息
     */
    private List<HospitalExtendParam> hospitalBranch;

    /**
     * 门诊类别
     */
    private List<HospitalExtendParam> outpatientType;

    /**
     * 挂号方式
     */
    private List<HospitalExtendParam> registrationMode;

    /**
     * 员工信息
     */
    private List<HospitalExtendParam> employeeInfo;


    public int getId() {
        return id;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public int getStatus() {
        return status;
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

    public List<HospitalExtendParam> getEmployeeInfo() {
        return employeeInfo;
    }

    @Override
    public String toString() {
        return "HospitalParam{" +
                "id=" + id +
                ", hospitalName='" + hospitalName + '\'' +
                ", status=" + status +
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
                ", employeeInfo=" + employeeInfo +
                '}';
    }
}
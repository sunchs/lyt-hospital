package com.sunchs.lyt.hospital.bean;

import java.util.List;

public class HospitalComplaintData {

    /**
     * 自增ID
     */
    private Integer id;

    /**
     * 医院ID
     */
    private Integer hospitalId;

    /**
     * 医院名称
     */
    private String hospitalName;

    /**
     * 科室类型ID
     */
    private Integer officeTypeId;

    /**
     * 科室类型名称
     */
    private String officeTypeName;

    /**
     * 科室ID
     */
    private Integer officeId;

    /**
     * 科室名称
     */
    private String officeName;

    /**
     * 投诉类型ID
     */
    private Integer typeId;

    /**
     * 投诉类型名称
     */
    private String typeName;

    /**
     * 姓名
     */
    private String name;

    /**
     * 联系电话
     */
    private String tel;

    /**
     * 就诊卡号
     */
    private String number;

    /**
     * 被投诉姓名或工号
     */
    private String respondent;

    /**
     * 投诉内容
     */
    private String content;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 附件列表
     */
    private List<String> fileList;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(Integer hospitalId) {
        this.hospitalId = hospitalId;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public Integer getOfficeTypeId() {
        return officeTypeId;
    }

    public void setOfficeTypeId(Integer officeTypeId) {
        this.officeTypeId = officeTypeId;
    }

    public String getOfficeTypeName() {
        return officeTypeName;
    }

    public void setOfficeTypeName(String officeTypeName) {
        this.officeTypeName = officeTypeName;
    }

    public Integer getOfficeId() {
        return officeId;
    }

    public void setOfficeId(Integer officeId) {
        this.officeId = officeId;
    }

    public String getOfficeName() {
        return officeName;
    }

    public void setOfficeName(String officeName) {
        this.officeName = officeName;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getRespondent() {
        return respondent;
    }

    public void setRespondent(String respondent) {
        this.respondent = respondent;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public List<String> getFileList() {
        return fileList;
    }

    public void setFileList(List<String> fileList) {
        this.fileList = fileList;
    }
}

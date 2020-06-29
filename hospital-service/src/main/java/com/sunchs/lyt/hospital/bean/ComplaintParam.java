package com.sunchs.lyt.hospital.bean;

import com.sunchs.lyt.framework.bean.PagingParam;
import com.sunchs.lyt.hospital.exception.HospitalException;

import java.util.List;
import java.util.Objects;

public class ComplaintParam extends PagingParam {

    /**
     * 医院ID
     */
    private int hospitalId;

    /**
     * 科室类型ID
     */
    private int officeTypeId;

    /**
     * 科室ID
     */
    private int officeId;

    /**
     * 投诉类型ID
     */
    private int typeId;

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
     * 开始时间
     */
    private String startTime;

    /**
     * 结束时间
     */
    private String endTime;

    /**
     * 上传文件列表
     */
    private List<String> fileList;

    public void filter() {
        // 姓名
        if (Objects.isNull(name) || name.length() == 0) {
            throw new HospitalException("姓名不能为空！");
        } else if (name.length() > 32) {
            throw new HospitalException("姓名字符长度不能超过32个字符！");
        }
        // 电话
        if (Objects.isNull(tel) || tel.length() == 0) {
            throw new HospitalException("联系电话不能为空！");
        } else if (tel.length() > 32) {
            throw new HospitalException("联系电话字符长度不能超过32个字符！");
        }
        // 医院
        if (hospitalId == 0) {
            throw new HospitalException("医院不能为空！");
        }
//        // 科室类型
//        if (officeTypeId == 0) {
//            throw new HospitalException("科室类型不能为空！");
//        }
        // 科室
        if (officeId == 0) {
            throw new HospitalException("科室不能为空！");
        }
        // 投诉对象
        if (Objects.isNull(respondent) || respondent.length() == 0) {
            throw new HospitalException("投诉对象不能为空！");
        } else if (respondent.length() > 64) {
            throw new HospitalException("投诉对象字符长度不能超过64个字符！");
        }
        // 投诉内容
        if (Objects.isNull(content) && content.length() == 0) {
            throw new HospitalException("投诉内容不能为空！");
        }
    }

    public int getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(int hospitalId) {
        this.hospitalId = hospitalId;
    }

    public int getOfficeTypeId() {
        return officeTypeId;
    }

    public void setOfficeTypeId(int officeTypeId) {
        this.officeTypeId = officeTypeId;
    }

    public int getOfficeId() {
        return officeId;
    }

    public void setOfficeId(int officeId) {
        this.officeId = officeId;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
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

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public List<String> getFileList() {
        return fileList;
    }

    public void setFileList(List<String> fileList) {
        this.fileList = fileList;
    }
}

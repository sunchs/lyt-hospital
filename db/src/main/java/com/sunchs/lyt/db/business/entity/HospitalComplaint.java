package com.sunchs.lyt.db.business.entity;

import com.baomidou.mybatisplus.enums.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 医院投诉表
 * </p>
 *
 * @author king
 * @since 2020-06-23
 */
@TableName("hospital_complaint")
public class HospitalComplaint extends Model<HospitalComplaint> {

    private static final long serialVersionUID = 1L;

    /**
     * 自增ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 医院ID
     */
    @TableField("hospital_id")
    private Integer hospitalId;

    /**
     * 科室类型ID
     */
    @TableField("office_type_id")
    private Integer officeTypeId;

    /**
     * 科室ID
     */
    @TableField("office_id")
    private Integer officeId;

    /**
     * 投诉类型ID
     */
    @TableField("type_id")
    private Integer typeId;

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
    @TableField("create_time")
    private Date createTime;

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
    public Integer getOfficeTypeId() {
        return officeTypeId;
    }

    public void setOfficeTypeId(Integer officeTypeId) {
        this.officeTypeId = officeTypeId;
    }
    public Integer getOfficeId() {
        return officeId;
    }

    public void setOfficeId(Integer officeId) {
        this.officeId = officeId;
    }
    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
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
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public static final String ID = "id";

    public static final String HOSPITAL_ID = "hospital_id";

    public static final String OFFICE_TYPE_ID = "office_type_id";

    public static final String OFFICE_ID = "office_id";

    public static final String TYPE_ID = "type_id";

    public static final String NAME = "name";

    public static final String TEL = "tel";

    public static final String NUMBER = "number";

    public static final String RESPONDENT = "respondent";

    public static final String CONTENT = "content";

    public static final String CREATE_TIME = "create_time";

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "HospitalComplaint{" +
        "id=" + id +
        ", hospitalId=" + hospitalId +
        ", officeTypeId=" + officeTypeId +
        ", officeId=" + officeId +
        ", typeId=" + typeId +
        ", name=" + name +
        ", tel=" + tel +
        ", number=" + number +
        ", respondent=" + respondent +
        ", content=" + content +
        ", createTime=" + createTime +
        "}";
    }
}

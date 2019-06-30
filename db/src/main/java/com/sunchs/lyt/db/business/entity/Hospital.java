package com.sunchs.lyt.db.business.entity;

import com.baomidou.mybatisplus.enums.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import java.io.Serializable;

/**
 * <p>
 * 医院信息表
 * </p>
 *
 * @author king
 * @since 2019-07-01
 */
public class Hospital extends Model<Hospital> {

    private static final long serialVersionUID = 1L;

    /**
     * 自增ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 医院名称
     */
    @TableField("hospital_name")
    private String hospitalName;

    /**
     * 医院类型
     */
    @TableField("hospital_type")
    private Integer hospitalType;

    /**
     * 是否开启
     */
    private Integer status;

    /**
     * 医院性质
     */
    @TableField("hospital_property")
    private Integer hospitalProperty;

    /**
     * 隶属
     */
    private Integer subjection;

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
    @TableField("contact_info")
    private String contactInfo;

    /**
     * 业务领导
     */
    @TableField("operation_name")
    private String operationName;

    /**
     * 业务电话
     */
    @TableField("operation_phone")
    private String operationPhone;

    /**
     * 开放床位
     */
    @TableField("open_beds")
    private Integer openBeds;

    /**
     * 备注
     */
    private String remarks;

    /**
     * 更新人ID
     */
    @TableField("update_id")
    private Integer updateId;

    /**
     * 更新时间
     */
    @TableField("update_time")
    private Date updateTime;

    /**
     * 创建人ID
     */
    @TableField("create_id")
    private Integer createId;

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
    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }
    public Integer getHospitalType() {
        return hospitalType;
    }

    public void setHospitalType(Integer hospitalType) {
        this.hospitalType = hospitalType;
    }
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
    public Integer getHospitalProperty() {
        return hospitalProperty;
    }

    public void setHospitalProperty(Integer hospitalProperty) {
        this.hospitalProperty = hospitalProperty;
    }
    public Integer getSubjection() {
        return subjection;
    }

    public void setSubjection(Integer subjection) {
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
    public Integer getOpenBeds() {
        return openBeds;
    }

    public void setOpenBeds(Integer openBeds) {
        this.openBeds = openBeds;
    }
    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
    public Integer getUpdateId() {
        return updateId;
    }

    public void setUpdateId(Integer updateId) {
        this.updateId = updateId;
    }
    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
    public Integer getCreateId() {
        return createId;
    }

    public void setCreateId(Integer createId) {
        this.createId = createId;
    }
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public static final String ID = "id";

    public static final String HOSPITAL_NAME = "hospital_name";

    public static final String HOSPITAL_TYPE = "hospital_type";

    public static final String STATUS = "status";

    public static final String HOSPITAL_PROPERTY = "hospital_property";

    public static final String SUBJECTION = "subjection";

    public static final String ADDRESS = "address";

    public static final String CONTACTS = "contacts";

    public static final String CONTACT_INFO = "contact_info";

    public static final String OPERATION_NAME = "operation_name";

    public static final String OPERATION_PHONE = "operation_phone";

    public static final String OPEN_BEDS = "open_beds";

    public static final String REMARKS = "remarks";

    public static final String UPDATE_ID = "update_id";

    public static final String UPDATE_TIME = "update_time";

    public static final String CREATE_ID = "create_id";

    public static final String CREATE_TIME = "create_time";

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "Hospital{" +
        "id=" + id +
        ", hospitalName=" + hospitalName +
        ", hospitalType=" + hospitalType +
        ", status=" + status +
        ", hospitalProperty=" + hospitalProperty +
        ", subjection=" + subjection +
        ", address=" + address +
        ", contacts=" + contacts +
        ", contactInfo=" + contactInfo +
        ", operationName=" + operationName +
        ", operationPhone=" + operationPhone +
        ", openBeds=" + openBeds +
        ", remarks=" + remarks +
        ", updateId=" + updateId +
        ", updateTime=" + updateTime +
        ", createId=" + createId +
        ", createTime=" + createTime +
        "}";
    }
}

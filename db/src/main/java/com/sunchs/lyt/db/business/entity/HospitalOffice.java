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
 * 医院科室表
 * </p>
 *
 * @author king
 * @since 2019-08-22
 */
@TableName("hospital_office")
public class HospitalOffice extends Model<HospitalOffice> {

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
     * 科室类型
     */
    private Integer type;

    /**
     * 组名
     */
    @TableField("group_name")
    private String groupName;

    /**
     * 科室标题
     */
    private String title;

    /**
     * 年数量
     */
    private Integer quantity;

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
    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
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

    public static final String ID = "id";

    public static final String HOSPITAL_ID = "hospital_id";

    public static final String TYPE = "type";

    public static final String GROUP_NAME = "group_name";

    public static final String TITLE = "title";

    public static final String QUANTITY = "quantity";

    public static final String UPDATE_ID = "update_id";

    public static final String UPDATE_TIME = "update_time";

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "HospitalOffice{" +
        "id=" + id +
        ", hospitalId=" + hospitalId +
        ", type=" + type +
        ", groupName=" + groupName +
        ", title=" + title +
        ", quantity=" + quantity +
        ", updateId=" + updateId +
        ", updateTime=" + updateTime +
        "}";
    }
}

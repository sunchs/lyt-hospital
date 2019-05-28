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
 * 
 * </p>
 *
 * @author king
 * @since 2019-05-28
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
     * 科室名称
     */
    private String name;

    /**
     * 年数量
     */
    @TableField("year_quantity")
    private Integer yearQuantity;

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
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public Integer getYearQuantity() {
        return yearQuantity;
    }

    public void setYearQuantity(Integer yearQuantity) {
        this.yearQuantity = yearQuantity;
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

    public static final String NAME = "name";

    public static final String YEAR_QUANTITY = "year_quantity";

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
        ", name=" + name +
        ", yearQuantity=" + yearQuantity +
        ", updateId=" + updateId +
        ", updateTime=" + updateTime +
        "}";
    }
}

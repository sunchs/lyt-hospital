package com.sunchs.lyt.db.business.entity;

import com.baomidou.mybatisplus.enums.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import java.io.Serializable;

/**
 * <p>
 * 问题表
 * </p>
 *
 * @author king
 * @since 2019-09-04
 */
public class Question extends Model<Question> {

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
     * 编号
     */
    private String number;

    /**
     * 标题
     */
    private String title;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 一级指标
     */
    @TableField("target_one")
    private Integer targetOne;

    /**
     * 二级指标
     */
    @TableField("target_two")
    private Integer targetTwo;

    /**
     * 三级指标
     */
    @TableField("target_three")
    private Integer targetThree;

    /**
     * 选项类型，选项模版的父ID
     */
    @TableField("option_type")
    private Integer optionType;

    /**
     * 答题的最大数量
     */
    @TableField("option_max_quantity")
    private Integer optionMaxQuantity;

    /**
     * 是否使用表情
     */
    @TableField("is_use_face")
    private Integer isUseFace;

    /**
     * 备注
     */
    private String remark;

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
    public Integer getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(Integer hospitalId) {
        this.hospitalId = hospitalId;
    }
    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
    public Integer getTargetOne() {
        return targetOne;
    }

    public void setTargetOne(Integer targetOne) {
        this.targetOne = targetOne;
    }
    public Integer getTargetTwo() {
        return targetTwo;
    }

    public void setTargetTwo(Integer targetTwo) {
        this.targetTwo = targetTwo;
    }
    public Integer getTargetThree() {
        return targetThree;
    }

    public void setTargetThree(Integer targetThree) {
        this.targetThree = targetThree;
    }
    public Integer getOptionType() {
        return optionType;
    }

    public void setOptionType(Integer optionType) {
        this.optionType = optionType;
    }
    public Integer getOptionMaxQuantity() {
        return optionMaxQuantity;
    }

    public void setOptionMaxQuantity(Integer optionMaxQuantity) {
        this.optionMaxQuantity = optionMaxQuantity;
    }
    public Integer getIsUseFace() {
        return isUseFace;
    }

    public void setIsUseFace(Integer isUseFace) {
        this.isUseFace = isUseFace;
    }
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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

    public static final String HOSPITAL_ID = "hospital_id";

    public static final String NUMBER = "number";

    public static final String TITLE = "title";

    public static final String STATUS = "status";

    public static final String TARGET_ONE = "target_one";

    public static final String TARGET_TWO = "target_two";

    public static final String TARGET_THREE = "target_three";

    public static final String OPTION_TYPE = "option_type";

    public static final String OPTION_MAX_QUANTITY = "option_max_quantity";

    public static final String IS_USE_FACE = "is_use_face";

    public static final String REMARK = "remark";

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
        return "Question{" +
        "id=" + id +
        ", hospitalId=" + hospitalId +
        ", number=" + number +
        ", title=" + title +
        ", status=" + status +
        ", targetOne=" + targetOne +
        ", targetTwo=" + targetTwo +
        ", targetThree=" + targetThree +
        ", optionType=" + optionType +
        ", optionMaxQuantity=" + optionMaxQuantity +
        ", isUseFace=" + isUseFace +
        ", remark=" + remark +
        ", updateId=" + updateId +
        ", updateTime=" + updateTime +
        ", createId=" + createId +
        ", createTime=" + createTime +
        "}";
    }
}

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
 * @since 2019-04-21
 */
public class Question extends Model<Question> {

    private static final long serialVersionUID = 1L;

    /**
     * 自增ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

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
     * 分类ID
     */
    @TableField("option_type")
    private Integer optionType;

    /**
     * 答题模式
     */
    @TableField("option_mode")
    private String optionMode;

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
    public String getOptionMode() {
        return optionMode;
    }

    public void setOptionMode(String optionMode) {
        this.optionMode = optionMode;
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

    public static final String TITLE = "title";

    public static final String STATUS = "status";

    public static final String TARGET_ONE = "target_one";

    public static final String TARGET_TWO = "target_two";

    public static final String TARGET_THREE = "target_three";

    public static final String OPTION_TYPE = "option_type";

    public static final String OPTION_MODE = "option_mode";

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
        ", title=" + title +
        ", status=" + status +
        ", targetOne=" + targetOne +
        ", targetTwo=" + targetTwo +
        ", targetThree=" + targetThree +
        ", optionType=" + optionType +
        ", optionMode=" + optionMode +
        ", remark=" + remark +
        ", updateId=" + updateId +
        ", updateTime=" + updateTime +
        ", createId=" + createId +
        ", createTime=" + createTime +
        "}";
    }
}

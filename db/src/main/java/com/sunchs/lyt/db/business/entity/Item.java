package com.sunchs.lyt.db.business.entity;

import com.baomidou.mybatisplus.enums.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author king
 * @since 2019-07-21
 */
public class Item extends Model<Item> {

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
     * 项目编号
     */
    private String number;

    /**
     * 项目标题
     */
    private String title;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 调查类型
     */
    private Integer checkType;

    /**
     * 是否分批
     */
    private Integer isBatch;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 调查次数
     */
    private Integer checkQty;

    /**
     * 第几次调查
     */
    private Integer nowQty;

    /**
     * 进场时间
     */
    private Date approachTime;

    /**
     * 交付时间
     */
    private Date deliveryTime;

    /**
     * 数据分析时间
     */
    private Date dataAnalysisTime;

    /**
     * 开始时间
     */
    private Date reportStartTime;

    /**
     * 结束时间
     */
    private Date reportEndTime;

    /**
     * 负责人用户ID
     */
    @TableField("user_id")
    private Integer userId;

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
    public Integer getCheckType() {
        return checkType;
    }

    public void setCheckType(Integer checkType) {
        this.checkType = checkType;
    }
    public Integer getIsBatch() {
        return isBatch;
    }

    public void setIsBatch(Integer isBatch) {
        this.isBatch = isBatch;
    }
    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }
    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
    public Integer getCheckQty() {
        return checkQty;
    }

    public void setCheckQty(Integer checkQty) {
        this.checkQty = checkQty;
    }
    public Integer getNowQty() {
        return nowQty;
    }

    public void setNowQty(Integer nowQty) {
        this.nowQty = nowQty;
    }
    public Date getApproachTime() {
        return approachTime;
    }

    public void setApproachTime(Date approachTime) {
        this.approachTime = approachTime;
    }
    public Date getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(Date deliveryTime) {
        this.deliveryTime = deliveryTime;
    }
    public Date getDataAnalysisTime() {
        return dataAnalysisTime;
    }

    public void setDataAnalysisTime(Date dataAnalysisTime) {
        this.dataAnalysisTime = dataAnalysisTime;
    }
    public Date getReportStartTime() {
        return reportStartTime;
    }

    public void setReportStartTime(Date reportStartTime) {
        this.reportStartTime = reportStartTime;
    }
    public Date getReportEndTime() {
        return reportEndTime;
    }

    public void setReportEndTime(Date reportEndTime) {
        this.reportEndTime = reportEndTime;
    }
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
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

    public static final String CHECKTYPE = "checkType";

    public static final String ISBATCH = "isBatch";

    public static final String STARTTIME = "startTime";

    public static final String ENDTIME = "endTime";

    public static final String CHECKQTY = "checkQty";

    public static final String NOWQTY = "nowQty";

    public static final String APPROACHTIME = "approachTime";

    public static final String DELIVERYTIME = "deliveryTime";

    public static final String DATAANALYSISTIME = "dataAnalysisTime";

    public static final String REPORTSTARTTIME = "reportStartTime";

    public static final String REPORTENDTIME = "reportEndTime";

    public static final String USER_ID = "user_id";

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
        return "Item{" +
        "id=" + id +
        ", hospitalId=" + hospitalId +
        ", number=" + number +
        ", title=" + title +
        ", status=" + status +
        ", checkType=" + checkType +
        ", isBatch=" + isBatch +
        ", startTime=" + startTime +
        ", endTime=" + endTime +
        ", checkQty=" + checkQty +
        ", nowQty=" + nowQty +
        ", approachTime=" + approachTime +
        ", deliveryTime=" + deliveryTime +
        ", dataAnalysisTime=" + dataAnalysisTime +
        ", reportStartTime=" + reportStartTime +
        ", reportEndTime=" + reportEndTime +
        ", userId=" + userId +
        ", updateId=" + updateId +
        ", updateTime=" + updateTime +
        ", createId=" + createId +
        ", createTime=" + createTime +
        "}";
    }
}

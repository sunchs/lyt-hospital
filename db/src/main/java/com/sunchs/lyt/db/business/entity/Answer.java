package com.sunchs.lyt.db.business.entity;

import com.baomidou.mybatisplus.enums.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import java.io.Serializable;

/**
 * <p>
 * 答案基本表
 * </p>
 *
 * @author king
 * @since 2019-07-29
 */
public class Answer extends Model<Answer> {

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
     * 项目ID
     */
    @TableField("item_id")
    private Integer itemId;

    /**
     * 科室ID，取项目科室ID
     */
    @TableField("office_id")
    private Integer officeId;

    /**
     * 问卷ID
     */
    @TableField("questionnaire_id")
    private Integer questionnaireId;

    /**
     * 负责人ID
     */
    @TableField("user_id")
    private Integer userId;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 处理原因
     */
    private String reason;

    /**
     * 答题使用时间
     */
    private Integer timeDuration;

    /**
     * 答题开始时间
     */
    private Date startTime;

    /**
     * 答题结束时间
     */
    private Date endTime;

    /**
     * 创建时间
     */
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
    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }
    public Integer getOfficeId() {
        return officeId;
    }

    public void setOfficeId(Integer officeId) {
        this.officeId = officeId;
    }
    public Integer getQuestionnaireId() {
        return questionnaireId;
    }

    public void setQuestionnaireId(Integer questionnaireId) {
        this.questionnaireId = questionnaireId;
    }
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
    public Integer getTimeDuration() {
        return timeDuration;
    }

    public void setTimeDuration(Integer timeDuration) {
        this.timeDuration = timeDuration;
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
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public static final String ID = "id";

    public static final String HOSPITAL_ID = "hospital_id";

    public static final String ITEM_ID = "item_id";

    public static final String OFFICE_ID = "office_id";

    public static final String QUESTIONNAIRE_ID = "questionnaire_id";

    public static final String USER_ID = "user_id";

    public static final String STATUS = "status";

    public static final String REASON = "reason";

    public static final String TIMEDURATION = "timeDuration";

    public static final String STARTTIME = "startTime";

    public static final String ENDTIME = "endTime";

    public static final String CREATETIME = "createTime";

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "Answer{" +
        "id=" + id +
        ", hospitalId=" + hospitalId +
        ", itemId=" + itemId +
        ", officeId=" + officeId +
        ", questionnaireId=" + questionnaireId +
        ", userId=" + userId +
        ", status=" + status +
        ", reason=" + reason +
        ", timeDuration=" + timeDuration +
        ", startTime=" + startTime +
        ", endTime=" + endTime +
        ", createTime=" + createTime +
        "}";
    }
}

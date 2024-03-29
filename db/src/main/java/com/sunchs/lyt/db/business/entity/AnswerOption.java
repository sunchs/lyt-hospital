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
 * 答案详情表
 * </p>
 *
 * @author king
 * @since 2019-12-30
 */
@TableName("answer_option")
public class AnswerOption extends Model<AnswerOption> {

    private static final long serialVersionUID = 1L;

    /**
     * 自增ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 答案ID
     */
    @TableField("answer_id")
    private Integer answerId;

    /**
     * 项目ID
     */
    @TableField("item_id")
    private Integer itemId;

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
     * 问卷ID
     */
    @TableField("questionnaire_id")
    private Integer questionnaireId;

    /**
     * 问题ID
     */
    @TableField("question_id")
    private Integer questionId;

    /**
     * 问题名称
     */
    @TableField("question_name")
    private String questionName;

    /**
     * 选项ID
     */
    @TableField("option_id")
    private Integer optionId;

    /**
     * 选项名称
     */
    @TableField("option_name")
    private String optionName;

    /**
     * 选项使用时间
     */
    private Integer timeDuration;

    /**
     * 选项开始时间
     */
    private Date startTime;

    /**
     * 选项结束时间
     */
    private Date endTime;

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
     * 分数
     */
    private Integer score;

    @TableField(exist = false)
    private Integer quantity;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public Integer getAnswerId() {
        return answerId;
    }

    public void setAnswerId(Integer answerId) {
        this.answerId = answerId;
    }
    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
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
    public Integer getQuestionnaireId() {
        return questionnaireId;
    }

    public void setQuestionnaireId(Integer questionnaireId) {
        this.questionnaireId = questionnaireId;
    }
    public Integer getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
    }
    public String getQuestionName() {
        return questionName;
    }

    public void setQuestionName(String questionName) {
        this.questionName = questionName;
    }
    public Integer getOptionId() {
        return optionId;
    }

    public void setOptionId(Integer optionId) {
        this.optionId = optionId;
    }
    public String getOptionName() {
        return optionName;
    }

    public void setOptionName(String optionName) {
        this.optionName = optionName;
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
    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public static final String ID = "id";

    public static final String ANSWER_ID = "answer_id";

    public static final String ITEM_ID = "item_id";

    public static final String OFFICE_TYPE_ID = "office_type_id";

    public static final String OFFICE_ID = "office_id";

    public static final String QUESTIONNAIRE_ID = "questionnaire_id";

    public static final String QUESTION_ID = "question_id";

    public static final String QUESTION_NAME = "question_name";

    public static final String OPTION_ID = "option_id";

    public static final String OPTION_NAME = "option_name";

    public static final String TIMEDURATION = "timeDuration";

    public static final String STARTTIME = "startTime";

    public static final String ENDTIME = "endTime";

    public static final String TARGET_ONE = "target_one";

    public static final String TARGET_TWO = "target_two";

    public static final String TARGET_THREE = "target_three";

    public static final String OPTION_TYPE = "option_type";

    public static final String SCORE = "score";

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "AnswerOption{" +
        "id=" + id +
        ", answerId=" + answerId +
        ", itemId=" + itemId +
        ", officeTypeId=" + officeTypeId +
        ", officeId=" + officeId +
        ", questionnaireId=" + questionnaireId +
        ", questionId=" + questionId +
        ", questionName=" + questionName +
        ", optionId=" + optionId +
        ", optionName=" + optionName +
        ", timeDuration=" + timeDuration +
        ", startTime=" + startTime +
        ", endTime=" + endTime +
        ", targetOne=" + targetOne +
        ", targetTwo=" + targetTwo +
        ", targetThree=" + targetThree +
        ", optionType=" + optionType +
        ", score=" + score +
        "}";
    }
}

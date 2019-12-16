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
 * @since 2019-12-17
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

    public static final String ID = "id";

    public static final String ANSWER_ID = "answer_id";

    public static final String ITEM_ID = "item_id";

    public static final String QUESTIONNAIRE_ID = "questionnaire_id";

    public static final String QUESTION_ID = "question_id";

    public static final String QUESTION_NAME = "question_name";

    public static final String OPTION_ID = "option_id";

    public static final String OPTION_NAME = "option_name";

    public static final String TIMEDURATION = "timeDuration";

    public static final String STARTTIME = "startTime";

    public static final String ENDTIME = "endTime";

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
        ", questionnaireId=" + questionnaireId +
        ", questionId=" + questionId +
        ", questionName=" + questionName +
        ", optionId=" + optionId +
        ", optionName=" + optionName +
        ", timeDuration=" + timeDuration +
        ", startTime=" + startTime +
        ", endTime=" + endTime +
        "}";
    }
}

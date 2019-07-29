package com.sunchs.lyt.db.business.entity;

import com.baomidou.mybatisplus.enums.IdType;
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
 * @since 2019-07-29
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

    public static final String ID = "id";

    public static final String ANSWER_ID = "answer_id";

    public static final String QUESTION_ID = "question_id";

    public static final String QUESTION_NAME = "question_name";

    public static final String OPTION_ID = "option_id";

    public static final String OPTION_NAME = "option_name";

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "AnswerOption{" +
        "id=" + id +
        ", answerId=" + answerId +
        ", questionId=" + questionId +
        ", questionName=" + questionName +
        ", optionId=" + optionId +
        ", optionName=" + optionName +
        "}";
    }
}

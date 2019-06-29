package com.sunchs.lyt.db.business.entity;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 问卷题目表
 * </p>
 *
 * @author king
 * @since 2019-06-29
 */
@TableName("questionnaire_extend")
public class QuestionnaireExtend extends Model<QuestionnaireExtend> {

    private static final long serialVersionUID = 1L;

    /**
     * 自增ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 问卷ID
     */
    @TableField("questionnaire_id")
    private Integer questionnaireId;

    /**
     * 题目ID
     */
    @TableField("question_id")
    private Integer questionId;

    /**
     * 跳转方式
     */
    @TableField("skip_mode")
    private Integer skipMode;

    /**
     * 跳转题目ID
     */
    @TableField("skip_question_id")
    private Integer skipQuestionId;

    /**
     * 跳转详情
     */
    @TableField("skip_content")
    private String skipContent;

    /**
     * 排序
     */
    private Integer sort;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
    public Integer getSkipMode() {
        return skipMode;
    }

    public void setSkipMode(Integer skipMode) {
        this.skipMode = skipMode;
    }
    public Integer getSkipQuestionId() {
        return skipQuestionId;
    }

    public void setSkipQuestionId(Integer skipQuestionId) {
        this.skipQuestionId = skipQuestionId;
    }
    public String getSkipContent() {
        return skipContent;
    }

    public void setSkipContent(String skipContent) {
        this.skipContent = skipContent;
    }
    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public static final String ID = "id";

    public static final String QUESTIONNAIRE_ID = "questionnaire_id";

    public static final String QUESTION_ID = "question_id";

    public static final String SKIP_MODE = "skip_mode";

    public static final String SKIP_QUESTION_ID = "skip_question_id";

    public static final String SKIP_CONTENT = "skip_content";

    public static final String SORT = "sort";

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "QuestionnaireExtend{" +
        "id=" + id +
        ", questionnaireId=" + questionnaireId +
        ", questionId=" + questionId +
        ", skipMode=" + skipMode +
        ", skipQuestionId=" + skipQuestionId +
        ", skipContent=" + skipContent +
        ", sort=" + sort +
        "}";
    }
}

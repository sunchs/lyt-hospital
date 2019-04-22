package com.sunchs.lyt.db.business.entity;

import com.baomidou.mybatisplus.enums.IdType;
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
 * @since 2019-04-21
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
    @TableField("wj_id")
    private Integer wjId;

    /**
     * 题目ID
     */
    @TableField("question_id")
    private Integer questionId;

    /**
     * 选项跳转字符串
     */
    @TableField("skip_content")
    private String skipContent;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public Integer getWjId() {
        return wjId;
    }

    public void setWjId(Integer wjId) {
        this.wjId = wjId;
    }
    public Integer getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
    }
    public String getSkipContent() {
        return skipContent;
    }

    public void setSkipContent(String skipContent) {
        this.skipContent = skipContent;
    }

    public static final String ID = "id";

    public static final String WJ_ID = "wj_id";

    public static final String QUESTION_ID = "question_id";

    public static final String SKIP_CONTENT = "skip_content";

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "QuestionnaireExtend{" +
        "id=" + id +
        ", wjId=" + wjId +
        ", questionId=" + questionId +
        ", skipContent=" + skipContent +
        "}";
    }
}

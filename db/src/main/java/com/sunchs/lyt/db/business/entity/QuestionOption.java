package com.sunchs.lyt.db.business.entity;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 题目选项表
 * </p>
 *
 * @author king
 * @since 2019-12-20
 */
@TableName("question_option")
public class QuestionOption extends Model<QuestionOption> {

    private static final long serialVersionUID = 1L;

    /**
     * 自增ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 问题ID
     */
    @TableField("question_id")
    private Integer questionId;

    /**
     * 标题
     */
    private String title;

    /**
     * 分数
     */
    private Integer score;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 模版ID
     */
    @TableField("template_id")
    private Integer templateId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public Integer getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }
    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }
    public Integer getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Integer templateId) {
        this.templateId = templateId;
    }

    public static final String ID = "id";

    public static final String QUESTION_ID = "question_id";

    public static final String TITLE = "title";

    public static final String SCORE = "score";

    public static final String SORT = "sort";

    public static final String TEMPLATE_ID = "template_id";

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "QuestionOption{" +
        "id=" + id +
        ", questionId=" + questionId +
        ", title=" + title +
        ", score=" + score +
        ", sort=" + sort +
        ", templateId=" + templateId +
        "}";
    }
}

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
     * 排序
     */
    private Integer sort;

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
    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public static final String ID = "id";

    public static final String QUESTION_ID = "question_id";

    public static final String TITLE = "title";

    public static final String SORT = "sort";

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
        ", sort=" + sort +
        "}";
    }
}

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
@TableName("question_tag_binding")
public class QuestionTagBinding extends Model<QuestionTagBinding> {

    private static final long serialVersionUID = 1L;

    /**
     * 自增ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 题目ID
     */
    @TableField("question_id")
    private Integer questionId;

    /**
     * 属性类型
     */
    @TableField("tag_type")
    private Integer tagType;

    /**
     * 属性ID
     */
    @TableField("tag_id")
    private Integer tagId;

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
    public Integer getTagType() {
        return tagType;
    }

    public void setTagType(Integer tagType) {
        this.tagType = tagType;
    }
    public Integer getTagId() {
        return tagId;
    }

    public void setTagId(Integer tagId) {
        this.tagId = tagId;
    }

    public static final String ID = "id";

    public static final String QUESTION_ID = "question_id";

    public static final String TAG_TYPE = "tag_type";

    public static final String TAG_ID = "tag_id";

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "QuestionTagBinding{" +
        "id=" + id +
        ", questionId=" + questionId +
        ", tagType=" + tagType +
        ", tagId=" + tagId +
        "}";
    }
}

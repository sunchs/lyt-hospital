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
@TableName("questionnaire_attribute")
public class QuestionnaireAttribute extends Model<QuestionnaireAttribute> {

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
     * 属性类型
     */
    @TableField("attr_type")
    private Integer attrType;

    /**
     * 属性ID
     */
    @TableField("attr_id")
    private Integer attrId;

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
    public Integer getAttrType() {
        return attrType;
    }

    public void setAttrType(Integer attrType) {
        this.attrType = attrType;
    }
    public Integer getAttrId() {
        return attrId;
    }

    public void setAttrId(Integer attrId) {
        this.attrId = attrId;
    }

    public static final String ID = "id";

    public static final String WJ_ID = "wj_id";

    public static final String QUESTION_ID = "question_id";

    public static final String ATTR_TYPE = "attr_type";

    public static final String ATTR_ID = "attr_id";

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "QuestionnaireAttribute{" +
        "id=" + id +
        ", wjId=" + wjId +
        ", questionId=" + questionId +
        ", attrType=" + attrType +
        ", attrId=" + attrId +
        "}";
    }
}

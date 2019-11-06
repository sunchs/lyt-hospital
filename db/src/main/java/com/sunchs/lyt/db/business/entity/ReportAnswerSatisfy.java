package com.sunchs.lyt.db.business.entity;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 答卷满意度表
 * </p>
 *
 * @author king
 * @since 2019-11-06
 */
@TableName("report_answer_satisfy")
public class ReportAnswerSatisfy extends Model<ReportAnswerSatisfy> {

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
     * 满意度
     */
    private Integer score;

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
    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
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

    public static final String ID = "id";

    public static final String HOSPITAL_ID = "hospital_id";

    public static final String ITEM_ID = "item_id";

    public static final String OFFICE_ID = "office_id";

    public static final String QUESTIONNAIRE_ID = "questionnaire_id";

    public static final String QUESTION_ID = "question_id";

    public static final String QUESTION_NAME = "question_name";

    public static final String SCORE = "score";

    public static final String TARGET_ONE = "target_one";

    public static final String TARGET_TWO = "target_two";

    public static final String TARGET_THREE = "target_three";

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "ReportAnswerSatisfy{" +
        "id=" + id +
        ", hospitalId=" + hospitalId +
        ", itemId=" + itemId +
        ", officeId=" + officeId +
        ", questionnaireId=" + questionnaireId +
        ", questionId=" + questionId +
        ", questionName=" + questionName +
        ", score=" + score +
        ", targetOne=" + targetOne +
        ", targetTwo=" + targetTwo +
        ", targetThree=" + targetThree +
        "}";
    }
}

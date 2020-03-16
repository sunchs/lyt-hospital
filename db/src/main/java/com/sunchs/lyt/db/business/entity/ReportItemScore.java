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
 * @since 2020-03-16
 */
@TableName("report_item_score")
public class ReportItemScore extends Model<ReportItemScore> {

    private static final long serialVersionUID = 1L;

    /**
     * 自增ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

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
     * 划分类型，1、按科室；2、按三级指标
     */
    @TableField("id_type")
    private Integer idType;

    /**
     * 划分ID
     */
    @TableField("id_value")
    private Integer idValue;

    /**
     * 得分
     */
    private Float score;

    @TableField(exist = false)
    private Integer qtyValue1;
    @TableField(exist = false)
    private Integer qtyValue2;
    @TableField(exist = false)
    private Integer qtyValue3;
    @TableField(exist = false)
    private Integer qtyValue4;
    @TableField(exist = false)
    private Integer qtyValue5;
    @TableField(exist = false)
    private Integer AnswerQuantity;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
    public Integer getIdType() {
        return idType;
    }

    public void setIdType(Integer idType) {
        this.idType = idType;
    }
    public Integer getIdValue() {
        return idValue;
    }

    public void setIdValue(Integer idValue) {
        this.idValue = idValue;
    }
    public Float getScore() {
        return score;
    }

    public void setScore(Float score) {
        this.score = score;
    }

    public Integer getQtyValue1() {
        return qtyValue1;
    }

    public void setQtyValue1(Integer qtyValue1) {
        this.qtyValue1 = qtyValue1;
    }

    public Integer getQtyValue2() {
        return qtyValue2;
    }

    public void setQtyValue2(Integer qtyValue2) {
        this.qtyValue2 = qtyValue2;
    }

    public Integer getQtyValue3() {
        return qtyValue3;
    }

    public void setQtyValue3(Integer qtyValue3) {
        this.qtyValue3 = qtyValue3;
    }

    public Integer getQtyValue4() {
        return qtyValue4;
    }

    public void setQtyValue4(Integer qtyValue4) {
        this.qtyValue4 = qtyValue4;
    }

    public Integer getQtyValue5() {
        return qtyValue5;
    }

    public void setQtyValue5(Integer qtyValue5) {
        this.qtyValue5 = qtyValue5;
    }

    public Integer getAnswerQuantity() {
        return AnswerQuantity;
    }

    public void setAnswerQuantity(Integer answerQuantity) {
        AnswerQuantity = answerQuantity;
    }

    public static final String ID = "id";

    public static final String ITEM_ID = "item_id";

    public static final String OFFICE_TYPE_ID = "office_type_id";

    public static final String ID_TYPE = "id_type";

    public static final String ID_VALUE = "id_value";

    public static final String SCORE = "score";

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "ReportItemScore{" +
        "id=" + id +
        ", itemId=" + itemId +
        ", officeTypeId=" + officeTypeId +
        ", idType=" + idType +
        ", idValue=" + idValue +
        ", score=" + score +
        "}";
    }
}

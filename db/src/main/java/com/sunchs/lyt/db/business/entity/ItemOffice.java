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
 * @since 2019-07-25
 */
@TableName("item_office")
public class ItemOffice extends Model<ItemOffice> {

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
     * 科室ID
     */
    @TableField("office_id")
    private Integer officeId;

    /**
     * 问卷ID
     */
    @TableField("questionnaire_id")
    private Integer questionnaireId;

    /**
     * 科室类型
     */
    private Integer type;

    /**
     * 科室标题
     */
    private String title;

    /**
     * 抽样量
     */
    private Integer quantity;

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
    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public static final String ID = "id";

    public static final String ITEM_ID = "item_id";

    public static final String OFFICE_ID = "office_id";

    public static final String QUESTIONNAIRE_ID = "questionnaire_id";

    public static final String TYPE = "type";

    public static final String TITLE = "title";

    public static final String QUANTITY = "quantity";

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "ItemOffice{" +
        "id=" + id +
        ", itemId=" + itemId +
        ", officeId=" + officeId +
        ", questionnaireId=" + questionnaireId +
        ", type=" + type +
        ", title=" + title +
        ", quantity=" + quantity +
        "}";
    }
}

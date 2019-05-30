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
 * @since 2019-05-30
 */
@TableName("items_office")
public class ItemsOffice extends Model<ItemsOffice> {

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

    public static final String ID = "id";

    public static final String ITEM_ID = "item_id";

    public static final String OFFICE_ID = "office_id";

    public static final String QUESTIONNAIRE_ID = "questionnaire_id";

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "ItemsOffice{" +
        "id=" + id +
        ", itemId=" + itemId +
        ", officeId=" + officeId +
        ", questionnaireId=" + questionnaireId +
        "}";
    }
}

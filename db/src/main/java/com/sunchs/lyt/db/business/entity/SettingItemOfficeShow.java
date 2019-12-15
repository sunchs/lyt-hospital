package com.sunchs.lyt.db.business.entity;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 科室显示字段设置表
 * </p>
 *
 * @author king
 * @since 2019-12-15
 */
@TableName("setting_item_office_show")
public class SettingItemOfficeShow extends Model<SettingItemOfficeShow> {

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
     * 显示位置
     */
    private Integer position;

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
    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public static final String ID = "id";

    public static final String ITEM_ID = "item_id";

    public static final String OFFICE_ID = "office_id";

    public static final String POSITION = "position";

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "SettingItemOfficeShow{" +
        "id=" + id +
        ", itemId=" + itemId +
        ", officeId=" + officeId +
        ", position=" + position +
        "}";
    }
}

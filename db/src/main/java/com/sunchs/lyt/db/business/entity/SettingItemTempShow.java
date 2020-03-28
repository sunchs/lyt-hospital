package com.sunchs.lyt.db.business.entity;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 
 * </p>
 *
 * @author king
 * @since 2019-12-22
 */
@TableName("setting_item_temp_show")
public class SettingItemTempShow extends Model<SettingItemTempShow> {

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
     * 科室类型
     */
    @TableField("office_type")
    private Integer officeType;

    /**
     * 科室ID集合
     */
    @TableField("office_ids")
    private String officeIds;

    /**
     * 指标ID集合
     */
    @TableField("target_ids")
    private String targetIds;

    @TableField(exist = false)
    private List<Integer> officeList;

    @TableField(exist = false)
    private List<Integer> targetList;

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
    public Integer getOfficeType() {
        return officeType;
    }

    public void setOfficeType(Integer officeType) {
        this.officeType = officeType;
    }
    public String getOfficeIds() {
        return officeIds;
    }

    public void setOfficeIds(String officeIds) {
        this.officeIds = officeIds;
    }
    public String getTargetIds() {
        return targetIds;
    }

    public void setTargetIds(String targetIds) {
        this.targetIds = targetIds;
    }

    public List<Integer> getOfficeList() {
        return officeList;
    }

    public void setOfficeList(List<Integer> officeList) {
        this.officeList = officeList;
    }

    public List<Integer> getTargetList() {
        return targetList;
    }

    public void setTargetList(List<Integer> targetList) {
        this.targetList = targetList;
    }

    public static final String ID = "id";

    public static final String ITEM_ID = "item_id";

    public static final String OFFICE_TYPE = "office_type";

    public static final String OFFICE_IDS = "office_ids";

    public static final String TARGET_IDS = "target_ids";

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "SettingItemTempShow{" +
        "id=" + id +
        ", itemId=" + itemId +
        ", officeType=" + officeType +
        ", officeIds=" + officeIds +
        ", targetIds=" + targetIds +
        "}";
    }
}

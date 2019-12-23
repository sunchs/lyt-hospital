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
 * @since 2019-12-23
 */
@TableName("setting_item_weight")
public class SettingItemWeight extends Model<SettingItemWeight> {

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
     * 二级指标
     */
    @TableField("target_two")
    private Integer targetTwo;

    /**
     * 权重
     */
    private Float weight;

    /**
     * 三级指标
     */
    @TableField("target_three")
    private String targetThree;

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
    public Integer getTargetTwo() {
        return targetTwo;
    }

    public void setTargetTwo(Integer targetTwo) {
        this.targetTwo = targetTwo;
    }
    public Float getWeight() {
        return weight;
    }

    public void setWeight(Float weight) {
        this.weight = weight;
    }
    public String getTargetThree() {
        return targetThree;
    }

    public void setTargetThree(String targetThree) {
        this.targetThree = targetThree;
    }

    public static final String ID = "id";

    public static final String ITEM_ID = "item_id";

    public static final String OFFICE_TYPE = "office_type";

    public static final String TARGET_TWO = "target_two";

    public static final String WEIGHT = "weight";

    public static final String TARGET_THREE = "target_three";

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "SettingItemWeight{" +
        "id=" + id +
        ", itemId=" + itemId +
        ", officeType=" + officeType +
        ", targetTwo=" + targetTwo +
        ", weight=" + weight +
        ", targetThree=" + targetThree +
        "}";
    }
}

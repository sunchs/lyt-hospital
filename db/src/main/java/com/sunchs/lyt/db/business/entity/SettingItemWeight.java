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
 * @since 2020-03-26
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
     * 三级指标
     */
    @TableField("target_three")
    private Integer targetThree;

    /**
     * 权重
     */
    private Float weight;

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
    public Integer getTargetThree() {
        return targetThree;
    }

    public void setTargetThree(Integer targetThree) {
        this.targetThree = targetThree;
    }
    public Float getWeight() {
        return weight;
    }

    public void setWeight(Float weight) {
        this.weight = weight;
    }

    public static final String ID = "id";

    public static final String ITEM_ID = "item_id";

    public static final String OFFICE_TYPE = "office_type";

    public static final String TARGET_TWO = "target_two";

    public static final String TARGET_THREE = "target_three";

    public static final String WEIGHT = "weight";

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
        ", targetThree=" + targetThree +
        ", weight=" + weight +
        "}";
    }
}

package com.sunchs.lyt.db.business.entity;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 医院投诉表
 * </p>
 *
 * @author king
 * @since 2020-06-23
 */
@TableName("hospital_complaint_type")
public class HospitalComplaintType extends Model<HospitalComplaintType> {

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
     * 标题
     */
    private String title;

    /**
     * 排序
     */
    private Integer sort;

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
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public static final String ID = "id";

    public static final String HOSPITAL_ID = "hospital_id";

    public static final String TITLE = "title";

    public static final String SORT = "sort";

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "HospitalComplaintType{" +
        "id=" + id +
        ", hospitalId=" + hospitalId +
        ", title=" + title +
        ", sort=" + sort +
        "}";
    }
}

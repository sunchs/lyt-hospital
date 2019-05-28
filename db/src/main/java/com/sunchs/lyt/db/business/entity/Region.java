package com.sunchs.lyt.db.business.entity;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.activerecord.Model;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author king
 * @since 2019-05-28
 */
public class Region extends Model<Region> {

    private static final long serialVersionUID = 1L;

    /**
     * 自增ID
     */
    @TableId(value = "region_id", type = IdType.AUTO)
    private Integer regionId;

    /**
     * 父ID
     */
    private Integer pid;

    /**
     * 标题
     */
    private String title;

    /**
     * 是否可用
     */
    private Integer enabled;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 备注
     */
    private String remarks;

    public Integer getRegionId() {
        return regionId;
    }

    public void setRegionId(Integer regionId) {
        this.regionId = regionId;
    }
    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public Integer getEnabled() {
        return enabled;
    }

    public void setEnabled(Integer enabled) {
        this.enabled = enabled;
    }
    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }
    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public static final String REGION_ID = "region_id";

    public static final String PID = "pid";

    public static final String TITLE = "title";

    public static final String ENABLED = "enabled";

    public static final String SORT = "sort";

    public static final String REMARKS = "remarks";

    @Override
    protected Serializable pkVal() {
        return this.regionId;
    }

    @Override
    public String toString() {
        return "Region{" +
        "regionId=" + regionId +
        ", pid=" + pid +
        ", title=" + title +
        ", enabled=" + enabled +
        ", sort=" + sort +
        ", remarks=" + remarks +
        "}";
    }
}

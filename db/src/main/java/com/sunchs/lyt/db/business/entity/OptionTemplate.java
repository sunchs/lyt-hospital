package com.sunchs.lyt.db.business.entity;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 选项模版表
 * </p>
 *
 * @author king
 * @since 2019-05-05
 */
@TableName("option_template")
public class OptionTemplate extends Model<OptionTemplate> {

    private static final long serialVersionUID = 1L;

    /**
     * 自增ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 父ID
     */
    private Integer pid;

    /**
     * 标题
     */
    private String content;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 备注
     */
    private String remarks;

    /**
     * 选项方式
     */
    private String mode;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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
    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public static final String ID = "id";

    public static final String PID = "pid";

    public static final String CONTENT = "content";

    public static final String SORT = "sort";

    public static final String REMARKS = "remarks";

    public static final String MODE = "mode";

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "OptionTemplate{" +
        "id=" + id +
        ", pid=" + pid +
        ", content=" + content +
        ", sort=" + sort +
        ", remarks=" + remarks +
        ", mode=" + mode +
        "}";
    }
}

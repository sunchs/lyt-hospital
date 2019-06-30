package com.sunchs.lyt.db.business.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 用户 - 医院 关系表
 * </p>
 *
 * @author king
 * @since 2019-07-01
 */
@TableName("user_hospital")
public class UserHospital extends Model<UserHospital> {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    @TableId("user_id")
    private Integer userId;

    /**
     * 医院ID
     */
    @TableField("hospital_id")
    private Integer hospitalId;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    public Integer getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(Integer hospitalId) {
        this.hospitalId = hospitalId;
    }

    public static final String USER_ID = "user_id";

    public static final String HOSPITAL_ID = "hospital_id";

    @Override
    protected Serializable pkVal() {
        return this.userId;
    }

    @Override
    public String toString() {
        return "UserHospital{" +
        "userId=" + userId +
        ", hospitalId=" + hospitalId +
        "}";
    }
}

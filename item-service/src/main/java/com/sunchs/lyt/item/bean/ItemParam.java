package com.sunchs.lyt.item.bean;

import com.sunchs.lyt.framework.bean.PagingList;
import com.sunchs.lyt.framework.util.NumberUtil;
import com.sunchs.lyt.framework.util.StringUtil;
import com.sunchs.lyt.item.exception.ItemException;

public class ItemParam extends PagingList {

    /**
     * ID
     */
    private int id;

    /**
     * 医院ID
     */
    private int hospitalId;

    /**
     * 项目编号
     */
    private String number;

    /**
     * 项目标题
     */
    private String title;

    /**
     * 调查类型
     */
    private int checkType;

    /**
     * 是否分批
     */
    private int isBatch;

    /**
     * 开始时间
     */
    private String startTime;

    /**
     * 结束时间
     */
    private String endTime;

    /**
     * 调查次数
     */
    private int checkQty;

    /**
     * 第几次调查
     */
    private int nowQty;

    /**
     * 进场时间
     */
    private String approachTime;

    /**
     * 交付时间
     */
    private String deliveryTime;

    /**
     * 数据分析时间
     */
    private String dataAnalysisTime;

    /**
     * 开始时间
     */
    private String reportStartTime;

    /**
     * 结束时间
     */
    private String reportEndTime;

    /**
     * 负责人用户ID
     */
    private int userId;

    public void filterParam() {
        if (StringUtil.isEmpty(number)) {
            throw new ItemException("项目编号不能为空");
        }

        if (StringUtil.isEmpty(title)) {
            throw new ItemException("项目标题不能为空");
        }

        if (NumberUtil.isZero(hospitalId)) {
            throw new ItemException("请选择项目方");
        }

        if (StringUtil.isEmpty(startTime)) {
            throw new ItemException("项目开始时间");
        }

        if (StringUtil.isEmpty(endTime)) {
            throw new ItemException("项目结束时间");
        }
    }

    public int getId() {
        return id;
    }

    public int getHospitalId() {
        return hospitalId;
    }

    public String getNumber() {
        return number;
    }

    public String getTitle() {
        return title;
    }

    public int getCheckType() {
        return checkType;
    }

    public int getIsBatch() {
        return isBatch;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public int getCheckQty() {
        return checkQty;
    }

    public int getNowQty() {
        return nowQty;
    }

    public String getApproachTime() {
        return approachTime;
    }

    public String getDeliveryTime() {
        return deliveryTime;
    }

    public String getDataAnalysisTime() {
        return dataAnalysisTime;
    }

    public String getReportStartTime() {
        return reportStartTime;
    }

    public String getReportEndTime() {
        return reportEndTime;
    }

    public int getUserId() {
        return userId;
    }
}
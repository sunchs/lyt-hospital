package com.sunchs.lyt.report.bean;

import java.util.List;

public class ItemAllSatisfySettingBean {

    /**
     * 科室类型
     */
    private Integer officeType;

    /**
     * 二级指标
     */
    private Integer targetTwo;

    /**
     * 权重
     */
    private Float weight;

    /**
     * 三级指标
     */
    private List<Integer> targetThree;

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

    public List<Integer> getTargetThree() {
        return targetThree;
    }

    public void setTargetThree(List<Integer> targetThree) {
        this.targetThree = targetThree;
    }
}

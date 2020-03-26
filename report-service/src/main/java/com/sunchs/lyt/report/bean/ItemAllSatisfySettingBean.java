package com.sunchs.lyt.report.bean;

import java.util.List;

public class ItemAllSatisfySettingBean {

    /**
     * 指标ID
     */
    private Integer targetId;

    /**
     * 权重
     */
    private Float weight;

    private List<ItemAllSatisfySettingBean> valueList;

    public Integer getTargetId() {
        return targetId;
    }

    public void setTargetId(Integer targetId) {
        this.targetId = targetId;
    }

    public Float getWeight() {
        return weight;
    }

    public void setWeight(Float weight) {
        this.weight = weight;
    }

    public List<ItemAllSatisfySettingBean> getValueList() {
        return valueList;
    }

    public void setValueList(List<ItemAllSatisfySettingBean> valueList) {
        this.valueList = valueList;
    }
}

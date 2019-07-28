package com.sunchs.lyt.item.bean;

import java.util.List;

public class BindOfficeParam {

    /**
     * 项目ID
     */
    private int itemId;

    /**
     * 绑定列表
     */
    private List<OfficeQuestionnaireParam> bindList;

    public int getItemId() {
        return itemId;
    }

    public List<OfficeQuestionnaireParam> getBindList() {
        return bindList;
    }
}

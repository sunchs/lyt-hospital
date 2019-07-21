package com.sunchs.lyt.item.bean;

import com.sunchs.lyt.db.business.entity.Item;

public class ItemData extends Item {

    private String statusName;

    private String finishTimeName;

    private String createTimeName;

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getFinishTimeName() {
        return finishTimeName;
    }

    public void setFinishTimeName(String finishTimeName) {
        this.finishTimeName = finishTimeName;
    }

    public String getCreateTimeName() {
        return createTimeName;
    }

    public void setCreateTimeName(String createTimeName) {
        this.createTimeName = createTimeName;
    }
}

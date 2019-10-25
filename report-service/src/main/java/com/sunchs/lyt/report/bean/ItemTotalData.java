package com.sunchs.lyt.report.bean;

import com.sunchs.lyt.db.business.entity.Item;

public class ItemTotalData extends Item {

    /**
     * 等待审核抽样量
     */
    private int waitQuantity;

    /**
     * 合格抽样量
     */
    private int passQuantity;

    public int getWaitQuantity() {
        return waitQuantity;
    }

    public void setWaitQuantity(int waitQuantity) {
        this.waitQuantity = waitQuantity;
    }

    public int getPassQuantity() {
        return passQuantity;
    }

    public void setPassQuantity(int passQuantity) {
        this.passQuantity = passQuantity;
    }
}

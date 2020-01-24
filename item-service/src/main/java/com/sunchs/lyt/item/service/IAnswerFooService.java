package com.sunchs.lyt.item.service;

import com.sunchs.lyt.item.bean.ItemOfficeFooData;
import com.sunchs.lyt.item.bean.SyncAnswerParam;

public interface IAnswerFooService {

    /**
     * 保存答卷
     */
    void saveAnswer(SyncAnswerParam param);

    /**
     * 获取项目科室详情
     */
    ItemOfficeFooData getItemOfficeInfo(int itemId, int officeId);
}

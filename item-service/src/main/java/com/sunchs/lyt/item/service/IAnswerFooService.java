package com.sunchs.lyt.item.service;

import com.sunchs.lyt.item.bean.AnswerParam;
import com.sunchs.lyt.item.bean.ItemOfficeFooData;

public interface IAnswerFooService {

    /**
     * 保存答卷
     */
    void saveAnswer(AnswerParam param);

    /**
     * 获取项目科室详情
     */
    ItemOfficeFooData getItemOfficeInfo(int itemId, int officeId);
}

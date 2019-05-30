package com.sunchs.lyt.item.service;

import com.sunchs.lyt.item.bean.ItemParam;
import com.sunchs.lyt.item.bean.OfficeQuestionnaireParam;

public interface IItemService {

    /**
     * 保存数据
     */
    int save(ItemParam param);

    /**
     * 项目 医院科室绑定问卷
     */
    void bindOfficeQuestionnaire(OfficeQuestionnaireParam param);

}

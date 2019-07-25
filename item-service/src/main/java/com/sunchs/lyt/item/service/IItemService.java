package com.sunchs.lyt.item.service;

import com.sunchs.lyt.framework.bean.PagingList;
import com.sunchs.lyt.item.bean.ItemData;
import com.sunchs.lyt.item.bean.ItemParam;
import com.sunchs.lyt.item.bean.OfficeQuestionnaireParam;

import java.util.List;
import java.util.Map;

public interface IItemService {

    /**
     * 项目分页列表
     */
    PagingList<ItemData> getPageList(ItemParam param);

    /**
     * 保存数据
     */
    int save(ItemParam param);

    /**
     * 项目 医院科室绑定问卷
     */
    void bindOfficeQuestionnaire(OfficeQuestionnaireParam param);

    /**
     * 获取项目所有科室
     */
    List<Map<String, Object>> getOfficeList(ItemParam param);
}

package com.sunchs.lyt.item.service;

import com.sunchs.lyt.framework.bean.PagingList;
import com.sunchs.lyt.item.bean.BindOfficeParam;
import com.sunchs.lyt.item.bean.ItemData;
import com.sunchs.lyt.item.bean.ItemParam;

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
    int bindOfficeQuestionnaire(BindOfficeParam param);

    /**
     * 根据 项目ID 获取项目详情
     */
    ItemData getById(int itemId);

    /**
     * 获取项目所有科室
     */
    List<Map<String, Object>> getOfficeList(ItemParam param);

    /**
     * 更新项目状态
     */
    void updateStatus(ItemParam param);
}

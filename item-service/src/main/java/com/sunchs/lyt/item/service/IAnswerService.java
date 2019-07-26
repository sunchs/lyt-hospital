package com.sunchs.lyt.item.service;

import com.sunchs.lyt.framework.bean.PagingList;
import com.sunchs.lyt.item.bean.AnswerParam;
import com.sunchs.lyt.item.bean.ItemData;

public interface IAnswerService {

    /**
     * 获取答案分页列表
     */
    PagingList<ItemData> getPageList(AnswerParam param);

    /**
     * 保存数据
     */
    int save(AnswerParam param);

}

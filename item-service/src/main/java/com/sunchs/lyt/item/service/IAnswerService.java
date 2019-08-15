package com.sunchs.lyt.item.service;

import com.sunchs.lyt.framework.bean.PagingList;
import com.sunchs.lyt.item.bean.AnswerData;
import com.sunchs.lyt.item.bean.AnswerParam;
import com.sunchs.lyt.item.bean.ItemParam;

public interface IAnswerService {

    /**
     * 获取答案分页列表
     */
    PagingList<AnswerData> getPageList(AnswerParam param);

    /**
     * 更新项目状态
     */
    void updateStatus(AnswerParam param);

    /**
     * 设置答卷不合格原因
     */
    void updateReason(AnswerParam param);
}

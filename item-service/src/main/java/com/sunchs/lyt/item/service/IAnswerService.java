package com.sunchs.lyt.item.service;

import com.sunchs.lyt.framework.bean.PagingList;
import com.sunchs.lyt.item.bean.AnswerData;
import com.sunchs.lyt.item.bean.AnswerParam;

public interface IAnswerService {

    /**
     * 获取答案分页列表
     */
    PagingList<AnswerData> getPageList(AnswerParam param);

    /**
     * 保存数据
     */
    int save(AnswerParam param);

}

package com.sunchs.lyt.report.service;

import com.sunchs.lyt.framework.bean.PagingList;
import com.sunchs.lyt.report.bean.AnswerQuestionData;
import com.sunchs.lyt.report.bean.AnswerQuestionParam;
import com.sunchs.lyt.report.bean.ItemTotalData;
import com.sunchs.lyt.report.bean.ItemTotalParam;

import java.util.List;

public interface IReportService {

    /**
     * 问卷抽样量统计列表
     */
    PagingList<ItemTotalData> getItemTotalList(ItemTotalParam param);

    /**
     * 每道题目占比例
     */
    List<AnswerQuestionData> getAnswerQuestionList(AnswerQuestionParam param);

}

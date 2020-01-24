package com.sunchs.lyt.report.service;

import com.sunchs.lyt.framework.bean.PagingList;
import com.sunchs.lyt.framework.bean.TitleData;
import com.sunchs.lyt.report.bean.*;

import java.util.List;
import java.util.Map;

public interface IReportService {

    /**
     * 问卷抽样量统计列表
     */
    PagingList<ItemTotalData> getItemTotalList(ItemTotalParam param);

    /**
     * 每道题目占比例
     */
    List<AnswerQuestionData> getAnswerQuestionList(AnswerQuestionParam param);

    /**
     * 项目已使用的科室
     */
    List<TitleData> getItemUseOffice(Integer itemId, Integer officeType);

    /**
     * 项目已使用的指标
     */
    List<TitleData> getItemUseTarget(Integer itemId, Integer officeType);

    /**
     * 保存项目科室显示设置
     */
    void saveSettingItemOffice(SettingParam param);

    /**
     * 保存项目指标显示设置
     */
    void saveSettingItemTarget(SettingParam param);

    /**
     * 获取项目人群标签
     */
    List<Map<String, Object>> getItemTagMenu(Integer itemId, Integer officeType);
}

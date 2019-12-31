package com.sunchs.lyt.report.service;

import com.sunchs.lyt.db.business.entity.Item;
import com.sunchs.lyt.db.business.entity.QuestionTarget;
import com.sunchs.lyt.framework.bean.TitleData;
import com.sunchs.lyt.framework.bean.TitleValueData;
import com.sunchs.lyt.report.bean.ItemCompareData;
import com.sunchs.lyt.report.bean.ItemCompareParam;

import java.util.List;

public interface IReportCompareService {

    /**
     * 根据 科室类型ID 获取题目满意度表的项目列表
     */
    List<Item> getItemListByOfficeType(Integer officeType);

    /**
     * 根据 项目ID、科室类型ID 获取题目满意度表的三级指标列表
     */
    List<QuestionTarget> getItemTargetThreeByOfficeType(Integer itemId, Integer officeType);

    /**
     * 获取项目对比信息
     */
    ItemCompareData getItemCompareInfo(ItemCompareParam param);

    /**
     * 获取指标对比信息
     */
    List<TitleValueData> getItemTargetCompareInfo(ItemCompareParam param);

    /**
     * 获取临时科室对比信息
     */
    ItemCompareData getItemTempOfficeCompareInfo(ItemCompareParam param);

    /**
     * 根据 项目ID集合 获取临时科室列表
     */
    List<TitleData> getTempOfficeByItemIds(ItemCompareParam param);

    /**
     * 根据 项目ID集合 获取自定义科室列表
     */
    List<TitleData> getCustomOfficeByItemIds(ItemCompareParam param);
}

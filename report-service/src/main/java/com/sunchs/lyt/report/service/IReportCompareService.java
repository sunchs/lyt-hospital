package com.sunchs.lyt.report.service;

import com.sunchs.lyt.db.business.entity.Item;
import com.sunchs.lyt.db.business.entity.QuestionTarget;
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
}

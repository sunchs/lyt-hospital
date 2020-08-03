package com.sunchs.lyt.report.controller;

import com.sunchs.lyt.framework.bean.RequestData;
import com.sunchs.lyt.framework.bean.ResultData;
import com.sunchs.lyt.framework.constants.CacheKeys;
import com.sunchs.lyt.framework.controller.BaseController;
import com.sunchs.lyt.framework.util.RedisUtil;
import com.sunchs.lyt.report.bean.*;
import com.sunchs.lyt.report.service.impl.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/report")
public class ReportController extends BaseController {

    @Autowired
    private ReportService reportService;

    @Autowired
    private ReportFactoryService reportFactoryService;

    @Autowired
    private ReportTagService reportTagService;

    @Autowired
    private ReportTargetService reportTargetService;

    @Autowired
    private ReportRelatedService reportRelatedService;

    @Autowired
    private ReportOptionService reportOptionService;

    @Autowired
    private ReportSingleOfficeService reportSingleOfficeService;

    /**
     * 问卷抽样量统计列表
     */
    @PostMapping("/itemTotalList")
    public ResultData getItemTotalList(@RequestBody RequestData data) {
        ItemTotalParam param = data.toObject(ItemTotalParam.class);
        return success(reportService.getItemTotalList(param));
    }

    /**
     * 问卷抽样量统计列表
     */
    @PostMapping("/answerQuestionList")
    public ResultData getAnswerQuestionList(@RequestBody RequestData data) {
        AnswerQuestionParam param = data.toObject(AnswerQuestionParam.class);
        return success(reportService.getAnswerQuestionList(param));
    }

    @PostMapping("/makeQuantitySatisfy")
    public ResultData makeQuantitySatisfy(@RequestBody RequestData data) {
        new Thread(() -> {
            reportFactoryService.makeAnswerQuantity(null);
//            if ( ! RedisUtil.exists(CacheKeys.MAKE_ANSWER_QUANTITY)) {
//                reportFactoryService.makeAnswerSatisfy();
//            }
        }).start();
        return success();
    }

    /**
     * 根据 标签ID 进行统计
     */
    @PostMapping("/itemTotalByTag")
    public ResultData getItemTotalByTag(@RequestBody RequestData data) {
        TotalParam param = data.toObject(TotalParam.class);
        return success(reportTagService.getItemQuantityByTag(param.getItemId(), param.getTagId(), param.getTargetOne()));
    }

    /**
     * 根据 指标ID 进行统计
     */
    @PostMapping("/itemTotalByTarget")
    public ResultData itemTotalByTarget(@RequestBody RequestData data) {
        TotalParam param = data.toObject(TotalParam.class);
        return success(reportTagService.getItemQuantityByTarget(param.getItemId(), param.getTargetId(), param.getPosition()));
    }

    /**
     * 根据 指标ID 进行统计满意度
     */
    @PostMapping("/itemSatisfyByTarget")
    public ResultData itemSatisfyByTarget(@RequestBody RequestData data) {
        TotalParam param = data.toObject(TotalParam.class);
        return success(reportTargetService.getItemSatisfyByTarget(param.getItemId(), param.getTargetId(), param.getPosition()));
    }

//    /**
//     * 获取 满意度、推荐度 的情况
//     */
//    @PostMapping("/getItemSatisfyByOnly")
//    public ResultData getItemSatisfyByOnly(@RequestBody RequestData data) {
//        TotalParam param = data.toObject(TotalParam.class);
//        return success(reportTargetService.getItemSatisfyByOnly(param.getItemId(), param.getTargetId(), param.getPosition()));
//    }

    /**
     * 获取总体满意度
     */
    @PostMapping("/getItemAllSatisfy")
    public ResultData getItemAllSatisfy(@RequestBody RequestData data) {
        Integer itemId = data.getInt("itemId");
        Integer officeType = data.getInt("officeType");
        return success(reportTargetService.getItemAllSatisfy(itemId, officeType));
    }

    /**
     * 获取可是满意度
     */
    @PostMapping("/itemOfficeSatisfy")
    public ResultData itemOfficeSatisfy(@RequestBody RequestData data) {
        TotalParam param = data.toObject(TotalParam.class);
        return success(reportTargetService.getItemOfficeSatisfy(param));
    }

    @PostMapping("/itemOfficeTargetSatisfy")
    public ResultData getItemOfficeTargetSatisfy(@RequestBody RequestData data) {
        TotalParam param = data.toObject(TotalParam.class);
        return success(reportTargetService.getItemOfficeTargetSatisfy(param));
    }
    
    /**
     * 项目已使用的科室
     */
    @PostMapping("/itemUseOffice")
    public ResultData getItemUseOffice(@RequestBody RequestData data) {
        Integer itemId = data.getInt("itemId");
        Integer officeType = data.getInt("officeType");
        return success(reportService.getItemUseOffice(itemId, officeType));
    }

    /**
     * 项目已使用的科室
     */
    @PostMapping("/itemUseTarget")
    public ResultData getItemUseTarget(@RequestBody RequestData data) {
        Integer itemId = data.getInt("itemId");
        Integer officeType = data.getInt("officeType");
        return success(reportService.getItemUseTarget(itemId, officeType));
    }

    /**
     * 科室显示字段设置
     */
    @PostMapping("/saveSettingItemOffice")
    public ResultData saveSettingItemOffice(@RequestBody RequestData data) {
        SettingParam param = data.toObject(SettingParam.class);
        reportService.saveSettingItemOffice(param);
        return success();
    }

    /**
     * 指标显示字段设置
     */
    @PostMapping("/saveSettingItemTarget")
    public ResultData saveSettingItemTarget(@RequestBody RequestData data) {
        SettingParam param = data.toObject(SettingParam.class);
        reportService.saveSettingItemTarget(param);
        return success();
    }

    @PostMapping("/getItemRelatedData")
    public ResultData getItemRelatedData(@RequestBody RequestData data) {
        Integer itemId = data.getInt("itemId");
        Integer officeType = data.getInt("officeType");
        return success(reportRelatedService.getItemRelatedData(itemId, officeType));
    }

//    /**
//     * 获取人群满意度，人群是通过 标签+选项 区分
//     */
//    @PostMapping("/getItemCrowdSatisfy")
//    public ResultData getItemCrowdSatisfy(@RequestBody RequestData data) {
//        ItemCrowdParam param = data.toObject(ItemCrowdParam.class);
//        return success(reportOptionService.getItemCrowdSatisfy(param));
//    }

    /**
     * 根据 选项ID 获取人群（答卷满意度）
     */
    @PostMapping("/getItemCrowdAnswerSatisfy")
    public ResultData getItemCrowdAnswerSatisfy(@RequestBody RequestData data) {
        ItemCrowdParam param = data.toObject(ItemCrowdParam.class);
        return success(reportOptionService.getItemCrowdAnswerSatisfy(param));
    }

    @PostMapping("/getItemTagMenu")
    public ResultData itemTagMenu(@RequestBody RequestData data) {
        int itemId = data.getInt("itemId");
        int officeType = data.getInt("officeType");
        return success(reportService.getItemTagMenu(itemId, officeType));
    }

    @PostMapping("/getItemSingleOfficeSatisfy")
    public ResultData getItemSingleOfficeSatisfy(@RequestBody RequestData data) {
        Integer itemId = data.getInt("itemId");
        Integer officeType = data.getInt("officeType");
        Integer officeId = data.getInt("officeId");
        return success(reportSingleOfficeService.getItemSingleOfficeSatisfyV2(itemId, officeType, officeId));
    }

    @PostMapping("/setItemOfficeRanking")
    public ResultData setItemOfficeRanking(@RequestBody RequestData data) {
        Integer itemId = data.getInt("itemId");
        reportSingleOfficeService.setItemOfficeRanking(itemId);
        return success();
    }

    /**
     * 更新项目数量
     */
    @PostMapping("/updateItemData")
    public ResultData updateItemData(@RequestBody RequestData data) {
        Integer itemId = data.getInt("itemId");
        reportFactoryService.makeAnswerQuantity(itemId);
        return success();
    }
}
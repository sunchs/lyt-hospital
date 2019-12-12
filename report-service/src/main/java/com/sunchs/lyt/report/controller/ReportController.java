package com.sunchs.lyt.report.controller;

import com.sunchs.lyt.framework.bean.RequestData;
import com.sunchs.lyt.framework.bean.ResultData;
import com.sunchs.lyt.framework.constants.CacheKeys;
import com.sunchs.lyt.framework.controller.BaseController;
import com.sunchs.lyt.framework.util.RedisUtil;
import com.sunchs.lyt.report.bean.AnswerQuestionParam;
import com.sunchs.lyt.report.bean.ItemTotalParam;
import com.sunchs.lyt.report.bean.TotalParam;
import com.sunchs.lyt.report.service.impl.ReportFactoryService;
import com.sunchs.lyt.report.service.impl.ReportService;
import com.sunchs.lyt.report.service.impl.ReportTagService;
import com.sunchs.lyt.report.service.impl.ReportTargetService;
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
            reportFactoryService.makeAnswerQuantity();
            if ( ! RedisUtil.exists(CacheKeys.MAKE_ANSWER_QUANTITY)) {
                reportFactoryService.makeAnswerSatisfy();
            }
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

    /**
     * 获取可是满意度
     */
    @PostMapping("/itemOfficeSatisfy")
    public ResultData itemOfficeSatisfy(@RequestBody RequestData data) {
        TotalParam param = data.toObject(TotalParam.class);
        return success(reportTargetService.getItemOfficeSatisfy(param));
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
}
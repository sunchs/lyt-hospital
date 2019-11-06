package com.sunchs.lyt.report.controller;

import com.sunchs.lyt.framework.bean.RequestData;
import com.sunchs.lyt.framework.bean.ResultData;
import com.sunchs.lyt.framework.constants.CacheKeys;
import com.sunchs.lyt.framework.controller.BaseController;
import com.sunchs.lyt.framework.util.RedisUtil;
import com.sunchs.lyt.report.bean.AnswerQuestionParam;
import com.sunchs.lyt.report.bean.ItemTotalParam;
import com.sunchs.lyt.report.service.impl.ReportFactoryService;
import com.sunchs.lyt.report.service.impl.ReportService;
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

}
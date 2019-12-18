package com.sunchs.lyt.report.controller;

import com.sunchs.lyt.framework.bean.RequestData;
import com.sunchs.lyt.framework.bean.ResultData;
import com.sunchs.lyt.framework.controller.BaseController;
import com.sunchs.lyt.report.bean.ItemSettingParam;
import com.sunchs.lyt.report.service.impl.ReportSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/setting")
public class ReportSettingController extends BaseController {

    @Autowired
    private ReportSettingService reportSettingService;

    /**
     * 获取项目已使用的问卷
     */
    @PostMapping("/getItemUseQuestionnaireList")
    public ResultData getItemUseQuestionnaireList(@RequestBody RequestData data) {
        ItemSettingParam param = data.toObject(ItemSettingParam.class);
        return success(reportSettingService.getItemUseQuestionnaireList(param.getItemId()));
    }

    /**
     * 获取问卷-题目-答案，三级联动
     */
    @PostMapping("/getItemUseAllList")
    public ResultData getItemUseAllList(@RequestBody RequestData data) {
        ItemSettingParam param = data.toObject(ItemSettingParam.class);
        return success(reportSettingService.getItemUseAllList(param.getItemId()));
    }

    /**
     * 根据 问卷ID 获取答卷中的指标
     */
    @PostMapping("/getQuestionnaireUseTarget")
    public ResultData getQuestionnaireUseTarget(@RequestBody RequestData data) {
        Integer itemId = data.getInt("itemId");
        Integer questionnaireId = data.getInt("questionnaireId");
        return success(reportSettingService.getItemQuestionnaireUseTarget(itemId, questionnaireId));
    }
}

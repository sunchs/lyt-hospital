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
    @PostMapping("/itemTotalList")
    public ResultData getItemTotalList(@RequestBody RequestData data) {
        ItemSettingParam param = data.toObject(ItemSettingParam.class);
        return success(reportSettingService.getItemUseQuestionnaireList(param.getItemId()));
    }

}

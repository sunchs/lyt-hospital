package com.sunchs.lyt.report.controller;

import com.sunchs.lyt.framework.bean.RequestData;
import com.sunchs.lyt.framework.bean.ResultData;
import com.sunchs.lyt.framework.controller.BaseController;
import com.sunchs.lyt.report.bean.ItemTotalParam;
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

    /**
     * 问卷抽样量统计列表
     */
    @PostMapping("/itemTotalList")
    public ResultData getPageList(@RequestBody RequestData data) {
        ItemTotalParam param = data.toObject(ItemTotalParam.class);
        return success(reportService.getItemTotalList(param));
    }

}
package com.sunchs.lyt.report.controller;

import com.sunchs.lyt.framework.bean.RequestData;
import com.sunchs.lyt.framework.bean.ResultData;
import com.sunchs.lyt.framework.controller.BaseController;
import com.sunchs.lyt.report.service.impl.ReportCustomOfficeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/custom")
public class ReportCustomController extends BaseController {

    @Autowired
    private ReportCustomOfficeService reportCustomOfficeService;

    @PostMapping("/getCustomOfficeList")
    public ResultData getCustomOfficeList(@RequestBody RequestData data) {
        Integer itemId = data.getInt("itemId");
        Integer officeType = data.getInt("officeType");
        return success(reportCustomOfficeService.getCustomOfficeList(itemId, officeType));
    }



}

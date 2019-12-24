package com.sunchs.lyt.report.controller;

import com.sunchs.lyt.framework.bean.RequestData;
import com.sunchs.lyt.framework.bean.ResultData;
import com.sunchs.lyt.framework.controller.BaseController;
import com.sunchs.lyt.report.bean.ItemCompareParam;
import com.sunchs.lyt.report.service.impl.ReportCompareService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/compare")
public class ReportCompareController extends BaseController {

    @Autowired
    private ReportCompareService reportCompareService;

    /**
     * 根据 科室类型ID 获取题目满意度表的项目列表
     */
    @PostMapping("/getItemListByOfficeType")
    public ResultData getItemListByOfficeType(@RequestBody RequestData data) {
        Integer officeType = data.getInt("officeType");
        return success(reportCompareService.getItemListByOfficeType(officeType));
    }

    /**
     *
     */
    @PostMapping("/getItemCompareInfo")
    public ResultData getItemCompareInfo(@RequestBody RequestData data) {
        ItemCompareParam param = data.toObject(ItemCompareParam.class);
        return success(reportCompareService.getItemCompareInfo(param));
    }
}

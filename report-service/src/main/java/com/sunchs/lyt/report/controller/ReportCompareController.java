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
     * 根据 项目ID、科室类型ID 获取题目满意度表的三级指标列表
     */
    @PostMapping("/getItemTargetThreeByOfficeType")
    public ResultData getItemTargetThreeByOfficeType(@RequestBody RequestData data) {
        Integer itemId = data.getInt("itemId");
        Integer officeType = data.getInt("officeType");
        return success(reportCompareService.getItemTargetThreeByOfficeType(itemId, officeType));
    }

    /**
     * 获取对比数据
     */
    @PostMapping("/getItemCompareInfo")
    public ResultData getItemCompareInfo(@RequestBody RequestData data) {
        ItemCompareParam param = data.toObject(ItemCompareParam.class);
        return success(reportCompareService.getItemCompareInfo(param));
    }

    /**
     * 获取指标对比数据
     */
    @PostMapping("/getItemTargetCompareInfo")
    public ResultData getItemTargetCompareInfo(@RequestBody RequestData data) {
        ItemCompareParam param = data.toObject(ItemCompareParam.class);
        return success(reportCompareService.getItemTargetCompareInfo(param));
    }

    /**
     * 获取临时科室对比数据
     */
    @PostMapping("/getItemTempOfficeCompareInfo")
    public ResultData getItemTempOfficeCompareInfo(@RequestBody RequestData data) {
        ItemCompareParam param = data.toObject(ItemCompareParam.class);
        return success(reportCompareService.getItemTempOfficeCompareInfo(param));
    }

    /**
     * 根据 项目ID集合 获取临时科室列表
     */
    @PostMapping("/getTempOfficeByItemIds")
    public ResultData getTempOfficeByItemIds(@RequestBody RequestData data) {
        ItemCompareParam param = data.toObject(ItemCompareParam.class);
        return success(reportCompareService.getTempOfficeByItemIds(param));
    }

    /**
     * 根据 项目ID集合 获取自定义科室列表
     */
    @PostMapping("/getCustomOfficeByItemIds")
    public ResultData getCustomOfficeByItemIds(@RequestBody RequestData data) {
        ItemCompareParam param = data.toObject(ItemCompareParam.class);
        return success(reportCompareService.getCustomOfficeByItemIds(param));
    }

    /**
     * 获取自定义科室对比数据
     */
    @PostMapping("/getItemCustomOfficeCompareInfo")
    public ResultData getItemCustomOfficeCompareInfo(@RequestBody RequestData data) {
        ItemCompareParam param = data.toObject(ItemCompareParam.class);
        return success(reportCompareService.getItemCustomOfficeCompareInfo(param));
    }

}

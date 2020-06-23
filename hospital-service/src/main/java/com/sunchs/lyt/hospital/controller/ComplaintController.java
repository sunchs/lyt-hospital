package com.sunchs.lyt.hospital.controller;

import com.sunchs.lyt.framework.bean.RequestData;
import com.sunchs.lyt.framework.bean.ResultData;
import com.sunchs.lyt.framework.controller.BaseController;
import com.sunchs.lyt.hospital.bean.ComplaintParam;
import com.sunchs.lyt.hospital.service.impl.ComplaintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/complaint")
public class ComplaintController extends BaseController {

    @Autowired
    private ComplaintService complaintService;

    /**
     * 新增 投诉
     */
    @PostMapping("/save")
    public ResultData save(@RequestBody RequestData data) {
        ComplaintParam param = data.toObject(ComplaintParam.class);
        complaintService.save(param);
        return success();
    }

    /**
     * 列表信息
     */
    @PostMapping("/getList")
    public ResultData getList(@RequestBody RequestData data) {
        ComplaintParam param = data.toObject(ComplaintParam.class);
        return success(complaintService.getList(param));
    }

    /**
     * 投诉类型列表信息
     */
    @PostMapping("/getTypeList")
    public ResultData getTypeList(@RequestBody RequestData data) {
        ComplaintParam param = data.toObject(ComplaintParam.class);
        return success(complaintService.getTypeList(param.getHospitalId()));
    }
}
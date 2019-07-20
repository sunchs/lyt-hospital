package com.sunchs.lyt.hospital.controller;

import com.sunchs.lyt.framework.bean.RequestData;
import com.sunchs.lyt.framework.bean.ResultData;
import com.sunchs.lyt.framework.controller.BaseController;
import com.sunchs.lyt.hospital.bean.HospitalParam;
import com.sunchs.lyt.hospital.service.impl.HospitalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hospital")
public class HospitalController extends BaseController {

    @Autowired
    private HospitalService hospitalService;

    /**
     * 医院信息 添加、编辑
     */
    @PostMapping("/save")
    public ResultData save(@RequestBody RequestData data) {
        HospitalParam param = data.toObject(HospitalParam.class);
        System.out.println(param);
        hospitalService.save(param);
        return success();
    }

    /**
     * 医院信息 删除
     */
    @PostMapping("/remove")
    public ResultData remove(@RequestBody RequestData data) {
        HospitalParam param = data.toObject(HospitalParam.class);
        return success();
    }

    /**
     * 医院信息分页列表
     */
    @PostMapping("/pageList")
    public ResultData getPageList(@RequestBody RequestData data) {
        HospitalParam param = data.toObject(HospitalParam.class);
        return success(hospitalService.getPageList(param));
    }

    /**
     * 根据 医院ID 获取医院详情
     */
    @PostMapping("/getById")
    public ResultData getById(@RequestBody RequestData data) {
        HospitalParam param = data.toObject(HospitalParam.class);
        return success(hospitalService.getById(param.getId()));
    }

    /**
     * 下拉菜单数据
     */
    @PostMapping("/selectData")
    public ResultData selectData(@RequestBody RequestData data) {
        return success(hospitalService.getSelectData());
    }
}
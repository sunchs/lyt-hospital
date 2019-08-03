package com.sunchs.lyt.hospital.controller;

import com.sunchs.lyt.db.business.entity.Item;
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
        hospitalService.save(param);
        return success();
    }

    /**
     * 根据 医院ID 更新状态
     */
    @PostMapping("/updateStatus")
    public ResultData remove(@RequestBody RequestData data) {
        HospitalParam param = data.toObject(HospitalParam.class);
        hospitalService.updateStatus(param);
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

    /**
     * 获取可用用户列表
     */
    @PostMapping("/usableList")
    public ResultData usableList(@RequestBody RequestData data) {
        return success(hospitalService.getUsableList());
    }

    /**
     * 获取医院所有科室
     */
    @PostMapping("/getOfficeList")
    public ResultData getOfficeList(@RequestBody RequestData data) {
        HospitalParam param = data.toObject(HospitalParam.class);
        return success(hospitalService.getOfficeList(param.getId()));
    }

    /**
     * 获取医院未绑定的科室
     */
    @PostMapping("/getNoBindOfficeList")
    public ResultData getNoBindOfficeList(@RequestBody RequestData data) {
        Integer itemId = data.getInt("itemId");
        Integer hospitalId = data.getInt("hospitalId");
        return success(hospitalService.getNoBindOfficeList(itemId, hospitalId));
    }

}
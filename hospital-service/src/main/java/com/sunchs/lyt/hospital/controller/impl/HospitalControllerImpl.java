package com.sunchs.lyt.hospital.controller.impl;

import com.sunchs.lyt.framework.bean.RequestData;
import com.sunchs.lyt.framework.bean.ResultData;
import com.sunchs.lyt.framework.controller.BaseController;
import com.sunchs.lyt.hospital.bean.HospitalParam;
import com.sunchs.lyt.hospital.controller.HospitalController;
import com.sunchs.lyt.hospital.service.impl.HospitalServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HospitalControllerImpl extends BaseController implements HospitalController {

    @Autowired
    private HospitalServiceImpl hospitalService;

    @Override
    public ResultData save(@RequestBody RequestData data) {
        HospitalParam param = data.toObject(HospitalParam.class);
        hospitalService.save(param);
        return success();
    }

    @Override
    public ResultData remove(RequestData data) {
        HospitalParam param = data.toObject(HospitalParam.class);
        return success();
    }
}
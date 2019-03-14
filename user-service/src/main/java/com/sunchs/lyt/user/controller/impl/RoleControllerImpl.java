package com.sunchs.lyt.user.controller.impl;

import com.sunchs.lyt.framework.bean.RequestData;
import com.sunchs.lyt.framework.bean.ResultData;
import com.sunchs.lyt.framework.controller.BaseController;
import com.sunchs.lyt.framework.util.NumberUtil;
import com.sunchs.lyt.user.bean.RoleParam;
import com.sunchs.lyt.user.controller.RoleController;
import com.sunchs.lyt.user.service.impl.RoleServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RoleControllerImpl extends BaseController implements RoleController {

    @Autowired
    RoleServiceImpl roleService;

    @Override
    public ResultData getRoleList(@RequestBody RequestData data) {
        return success(roleService.getRoleList());
    }

    @Override
    public ResultData save(@RequestBody RequestData data) {
        RoleParam param = data.toObject(RoleParam.class);
        if (NumberUtil.isZero(param.getRoleId())) {
            roleService.addRoleData(param);
        } else {
            roleService.updateRoleData(param);
        }

        return null;
    }
}
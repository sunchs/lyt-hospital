package com.sunchs.lyt.user.controller.impl;

import com.sunchs.lyt.framework.bean.RequestData;
import com.sunchs.lyt.framework.bean.ResultData;
import com.sunchs.lyt.framework.controller.BaseController;
import com.sunchs.lyt.user.bean.UserParam;
import com.sunchs.lyt.user.controller.UserController;
import com.sunchs.lyt.user.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserControllerImpl extends BaseController implements UserController {

    @Autowired
    UserServiceImpl userService;

    @Override
    public ResultData login(@RequestBody RequestData data) {
        UserParam param = data.toObject(UserParam.class);
        return success(userService.login(param));
    }

    @Override
    public ResultData logout(@RequestBody RequestData data) {
        return success(userService.logout());
    }

    @Override
    public ResultData save(@RequestBody RequestData data) {
        UserParam param = data.toObject(UserParam.class);
        return success(userService.saveAccount(param));
    }

}



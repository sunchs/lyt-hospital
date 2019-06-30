package com.sunchs.lyt.user.controller;

import com.sunchs.lyt.framework.bean.RequestData;
import com.sunchs.lyt.framework.bean.ResultData;
import com.sunchs.lyt.framework.controller.BaseController;
import com.sunchs.lyt.user.bean.UserParam;
import com.sunchs.lyt.user.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController extends BaseController {

    @Autowired
    UserService userService;

    /**
     * 用户列表
     */
    @PostMapping("/pageList")
    public ResultData getList(@RequestBody RequestData data) {
        UserParam param = data.toObject(UserParam.class);
        return success(userService.getPagingList(param));
    }

    /**
     * 登录系统
     */
    @PostMapping("/login")
    public ResultData login(@RequestBody RequestData data) {
        UserParam param = data.toObject(UserParam.class);
        return success(userService.login(param));
    }

    /**
     * 退出系统
     */
    @PostMapping("/logout")
    public ResultData logout(@RequestBody RequestData data) {
        return success(userService.logout());
    }

    /**
     * 添加账号
     */
    @PostMapping("/save")
    public ResultData save(@RequestBody RequestData data) {
        UserParam param = data.toObject(UserParam.class);
        return success(userService.save(param));
    }

}



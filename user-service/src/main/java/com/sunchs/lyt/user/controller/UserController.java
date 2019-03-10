package com.sunchs.lyt.user.controller;

import com.sunchs.lyt.framework.bean.RequestData;
import com.sunchs.lyt.framework.bean.ResultData;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/user")
public interface UserController
{
    /**
     * 登录系统
     */
    @PostMapping("/login")
    ResultData login(@RequestBody RequestData data);

    /**
     * 退出系统
     */
    @PostMapping("/logout")
    ResultData logout(@RequestBody RequestData data);

    /**
     * 注册账号密码
     */
    @PostMapping("/register")
    ResultData register(@RequestBody RequestData data);


}
package com.sunchs.lyt.user.controller;

import com.sunchs.lyt.framework.bean.RequestData;
import com.sunchs.lyt.framework.bean.ResultData;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/role")
public interface RoleController {

    /**
     * 角色列表数据
     */
    @PostMapping("/list")
    ResultData getRoleList(@RequestBody RequestData data);
}

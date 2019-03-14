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

    /**
     * 角色 添加/编辑
     */
    @PostMapping("/save")
    ResultData save(@RequestBody RequestData data);

//    /**
//     * 角色 删除
//     */
//    ResultData delete(@RequestBody RequestData data);
}

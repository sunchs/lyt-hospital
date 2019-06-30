package com.sunchs.lyt.user.controller;

import com.sunchs.lyt.framework.bean.RequestData;
import com.sunchs.lyt.framework.bean.ResultData;
import com.sunchs.lyt.framework.controller.BaseController;
import com.sunchs.lyt.user.bean.RoleParam;
import com.sunchs.lyt.user.service.impl.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/role")
public class RoleController extends BaseController {

    @Autowired
    RoleService roleService;

    /**
     * 角色列表数据
     */
    @PostMapping("/list")
    public ResultData getList(@RequestBody RequestData data) {
        return success(roleService.getRoleList());
    }

    /**
     * 角色 添加/编辑
     */
    @PostMapping("/save")
    public ResultData save(@RequestBody RequestData data) {
        RoleParam param = data.toObject(RoleParam.class);
        return success(roleService.save(param));
    }

    /**
     * 下拉菜单数据
     */
    @PostMapping("/selectData")
    public ResultData selectData(@RequestBody RequestData data) {
        return success(roleService.getSelectData());
    }
}
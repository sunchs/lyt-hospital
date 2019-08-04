package com.sunchs.lyt.user.controller;

import com.sunchs.lyt.framework.bean.RequestData;
import com.sunchs.lyt.framework.bean.ResultData;
import com.sunchs.lyt.framework.controller.BaseController;
import com.sunchs.lyt.user.service.impl.NodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/node")
public class NodeController extends BaseController {

    @Autowired
    NodeService nodeService;

    /**
     * 节点列表
     */
    @PostMapping("/list")
    public ResultData getList(@RequestBody RequestData data) {
        return success(nodeService.getList());
    }

    @PostMapping("/menuList")
    public ResultData getMenuList(@RequestBody RequestData data) {
        return success(nodeService.getMenuList());
    }

    @PostMapping("/roleNodeIds")
    public ResultData getRoleNodeIds(@RequestBody RequestData data) {
        int releId = data.getInt("id");
        return success(nodeService.getRoleNodeIds(releId));
    }
}

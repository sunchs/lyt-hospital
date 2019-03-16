package com.sunchs.lyt.user.controller.impl;

import com.sunchs.lyt.framework.bean.RequestData;
import com.sunchs.lyt.framework.bean.ResultData;
import com.sunchs.lyt.framework.controller.BaseController;
import com.sunchs.lyt.user.controller.NodeController;
import com.sunchs.lyt.user.service.impl.NodeServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NodeControllerImpl extends BaseController implements NodeController {

    @Autowired
    NodeServiceImpl nodeService;

    @Override
    public ResultData getList(@RequestBody RequestData data) {
        return success(nodeService.getList());
    }
}

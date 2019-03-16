package com.sunchs.lyt.user.controller;

import com.sunchs.lyt.framework.bean.RequestData;
import com.sunchs.lyt.framework.bean.ResultData;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


@RequestMapping("/node")
public interface NodeController {

    /**
     * 节点列表
     */
    @PostMapping("/list")
    ResultData getList(@RequestBody RequestData data);
}

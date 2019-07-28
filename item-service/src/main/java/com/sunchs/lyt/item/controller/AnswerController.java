package com.sunchs.lyt.item.controller;

import com.sunchs.lyt.framework.bean.RequestData;
import com.sunchs.lyt.framework.bean.ResultData;
import com.sunchs.lyt.framework.controller.BaseController;
import com.sunchs.lyt.item.bean.AnswerParam;
import com.sunchs.lyt.item.bean.ItemParam;
import com.sunchs.lyt.item.service.impl.AnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/answer")
public class AnswerController extends BaseController {

    @Autowired
    private AnswerService answerService;

    /**
     * 答案分页列表
     */
    @PostMapping("/pageList")
    public ResultData getPageList(@RequestBody RequestData data) {
        AnswerParam param = data.toObject(AnswerParam.class);
        return success(answerService.getPageList(param));
    }


}
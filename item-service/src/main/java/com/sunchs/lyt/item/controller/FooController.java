package com.sunchs.lyt.item.controller;

import com.sunchs.lyt.framework.bean.RequestData;
import com.sunchs.lyt.framework.bean.ResultData;
import com.sunchs.lyt.framework.controller.BaseController;
import com.sunchs.lyt.item.bean.AnswerParam;
import com.sunchs.lyt.item.bean.SyncAnswerParam;
import com.sunchs.lyt.item.service.impl.AnswerFooService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/foo")
public class FooController extends BaseController {

    @Autowired
    private AnswerFooService answerFooService;

    /**
     * 保存答案
     */
    @PostMapping("/saveAnswer")
    public ResultData saveAnswer(@RequestBody RequestData data) {
        SyncAnswerParam param = data.toObject(SyncAnswerParam.class);
        answerFooService.saveAnswer(param);
        return success();
    }

    /**
     * 保存答案
     */
    @PostMapping("/getItemOfficeInfo")
    public ResultData getItemOfficeInfo(@RequestBody RequestData data) {
        int itemId = data.getInt("itemId");
        int officeId = data.getInt("officeId");
        return success(answerFooService.getItemOfficeInfo(itemId, officeId));
    }
}
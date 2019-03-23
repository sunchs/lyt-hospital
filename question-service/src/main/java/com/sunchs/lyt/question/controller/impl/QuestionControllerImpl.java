package com.sunchs.lyt.question.controller.impl;

import com.sunchs.lyt.framework.bean.RequestData;
import com.sunchs.lyt.framework.bean.ResultData;
import com.sunchs.lyt.framework.controller.BaseController;
import com.sunchs.lyt.question.bean.QuestionParam;
import com.sunchs.lyt.question.controller.QuestionController;
import com.sunchs.lyt.question.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class QuestionControllerImpl extends BaseController implements QuestionController {

    @Autowired
    QuestionService questionService;

    @Override
    public ResultData save(@RequestBody RequestData data) {
        QuestionParam param = data.toObject(QuestionParam.class);
        return success(questionService.save(param));
    }

    @Override
    public ResultData getPageList(@RequestBody RequestData data) {
        QuestionParam param = data.toObject(QuestionParam.class);
        return success(questionService.getPageList(param));
    }
}
package com.sunchs.lyt.question.controller.impl;

import com.sunchs.lyt.framework.bean.RequestData;
import com.sunchs.lyt.framework.bean.ResultData;
import com.sunchs.lyt.framework.controller.BaseController;
import com.sunchs.lyt.question.bean.QuestionOptionParam;
import com.sunchs.lyt.question.controller.QuestionOptionController;
import com.sunchs.lyt.question.service.IQuestionOptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class QuestionOptionControllerImpl extends BaseController implements QuestionOptionController {

    @Autowired
    IQuestionOptionService IQuestionOptionService;

    @Override
    public ResultData save(@RequestBody RequestData data) {
        QuestionOptionParam param = data.toObject(QuestionOptionParam.class);
        IQuestionOptionService.save(param);
        return success();
    }

    @Override
    public ResultData getById(@RequestBody RequestData data) {
        QuestionOptionParam param = data.toObject(QuestionOptionParam.class);
        return success(IQuestionOptionService.getInfo(param));
    }

    @Override
    public ResultData getList(@RequestBody RequestData data) {
        QuestionOptionParam param = data.toObject(QuestionOptionParam.class);
        return success(IQuestionOptionService.getList(param));
    }
}
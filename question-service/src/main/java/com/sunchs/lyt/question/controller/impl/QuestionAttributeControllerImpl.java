package com.sunchs.lyt.question.controller.impl;

import com.sunchs.lyt.framework.bean.RequestData;
import com.sunchs.lyt.framework.bean.ResultData;
import com.sunchs.lyt.framework.controller.BaseController;
import com.sunchs.lyt.question.bean.QuestionTagParam;
import com.sunchs.lyt.question.controller.QuestionAttributeController;
import com.sunchs.lyt.question.service.impl.QuestionAttributeServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class QuestionAttributeControllerImpl extends BaseController implements QuestionAttributeController {

    @Autowired
    QuestionAttributeServiceImpl questionAttributeService;

    @Override
    public ResultData getById(@RequestBody RequestData data) {
        QuestionTagParam param = data.toObject(QuestionTagParam.class);
        return success(questionAttributeService.getById(param.getId()));
    }

    @Override
    public ResultData getList(@RequestBody RequestData data) {
        QuestionTagParam param = data.toObject(QuestionTagParam.class);
        return success(questionAttributeService.getList(param.getId()));
    }

    @Override
    public ResultData save(@RequestBody RequestData data) {
        QuestionTagParam param = data.toObject(QuestionTagParam.class);
        return success(questionAttributeService.save(param));
    }
}

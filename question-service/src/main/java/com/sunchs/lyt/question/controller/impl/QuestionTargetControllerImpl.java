package com.sunchs.lyt.question.controller.impl;

import com.sunchs.lyt.framework.bean.RequestData;
import com.sunchs.lyt.framework.bean.ResultData;
import com.sunchs.lyt.framework.controller.BaseController;
import com.sunchs.lyt.question.bean.QuestionTargetParam;
import com.sunchs.lyt.question.controller.QuestionTargetController;
import com.sunchs.lyt.question.service.impl.QuestionTargetServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class QuestionTargetControllerImpl extends BaseController implements QuestionTargetController {

    @Autowired
    QuestionTargetServiceImpl questionTargetService;

    @Override
    public ResultData getById(@RequestBody RequestData data) {
        QuestionTargetParam param = data.toObject(QuestionTargetParam.class);
        return success(questionTargetService.getById(param.getId()));
    }

    @Override
    public ResultData getList(@RequestBody RequestData data) {
        QuestionTargetParam param = data.toObject(QuestionTargetParam.class);
        return success(questionTargetService.getList(param.getId()));
    }

    @Override
    public ResultData save(@RequestBody RequestData data) {
        QuestionTargetParam param = data.toObject(QuestionTargetParam.class);
        return success(questionTargetService.save(param));
    }
}

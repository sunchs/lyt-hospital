package com.sunchs.lyt.question.controller.impl;

import com.sunchs.lyt.framework.bean.RequestData;
import com.sunchs.lyt.framework.bean.ResultData;
import com.sunchs.lyt.framework.controller.BaseController;
import com.sunchs.lyt.framework.util.MailUtil;
import com.sunchs.lyt.question.bean.QuestionParam;
import com.sunchs.lyt.question.controller.QuestionController;
import com.sunchs.lyt.question.service.impl.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class QuestionControllerImpl extends BaseController implements QuestionController {

    @Autowired
    QuestionService questionService;

    @Override
    public ResultData save(@RequestBody RequestData data) {
        QuestionParam param = data.toObject(QuestionParam.class);
        questionService.save(param);
        return success();
    }

    @Override
    public ResultData getPageList(@RequestBody RequestData data) {
        QuestionParam param = data.toObject(QuestionParam.class);
        return success(questionService.getPageList(param));
    }
}
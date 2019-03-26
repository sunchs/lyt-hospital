package com.sunchs.lyt.question.controller.impl;

import com.sunchs.lyt.framework.bean.RequestData;
import com.sunchs.lyt.framework.bean.ResultData;
import com.sunchs.lyt.framework.controller.BaseController;
import com.sunchs.lyt.question.bean.QuestionnaireParam;
import com.sunchs.lyt.question.controller.QuestionnaireController;
import com.sunchs.lyt.question.service.impl.QuestionnaireServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class QuestionnaireControllerImpl extends BaseController implements QuestionnaireController {

    @Autowired
    QuestionnaireServiceImpl questionnaireService;

    @Override
    public ResultData save(@RequestBody RequestData data) {
        QuestionnaireParam param = data.toObject(QuestionnaireParam.class);
        return success(questionnaireService.save(param));
    }

    @Override
    public ResultData getPageList(@RequestBody RequestData data) {
        QuestionnaireParam param = data.toObject(QuestionnaireParam.class);
        return success(questionnaireService.getPageList(param));
    }
}

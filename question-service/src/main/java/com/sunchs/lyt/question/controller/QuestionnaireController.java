package com.sunchs.lyt.question.controller;

import com.sunchs.lyt.framework.bean.RequestData;
import com.sunchs.lyt.framework.bean.ResultData;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/questionnaire")
public interface QuestionnaireController {

    /**
     * 问卷 添加、编辑
     */
    @PostMapping("/save")
    ResultData save(@RequestBody RequestData data);

    /**
     * 问卷列表
     */
    @PostMapping("/pageList")
    ResultData getPageList(@RequestBody RequestData data);

}

package com.sunchs.lyt.question.controller;

import com.sunchs.lyt.framework.bean.RequestData;
import com.sunchs.lyt.framework.bean.ResultData;
import com.sunchs.lyt.framework.controller.BaseController;
import com.sunchs.lyt.question.bean.QuestionParam;
import com.sunchs.lyt.question.service.impl.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/question")
public class QuestionController extends BaseController {

    @Autowired
    QuestionService questionService;

    /**
     * 题目 添加、编辑
     */
    @PostMapping("/save")
    public ResultData save(@RequestBody RequestData data) {
        QuestionParam param = data.toObject(QuestionParam.class);
        questionService.save(param);
        return success();
    }

    /**
     * 题目列表
     */
    @PostMapping("/pageList")
    public ResultData getPageList(@RequestBody RequestData data) {
        QuestionParam param = data.toObject(QuestionParam.class);
        return success(questionService.getPageList(param));
    }
}
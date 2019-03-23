package com.sunchs.lyt.question.controller;

import com.sunchs.lyt.framework.bean.RequestData;
import com.sunchs.lyt.framework.bean.ResultData;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/question")
public interface QuestionController
{
    /**
     * 题目 添加、编辑
     */
    @PostMapping("/save")
    ResultData save(@RequestBody RequestData data);

    /**
     * 题目列表
     */
    @PostMapping("/pageList")
    ResultData getPageList(@RequestBody RequestData data);
}
package com.sunchs.lyt.question.controller;

import com.sunchs.lyt.framework.bean.RequestData;
import com.sunchs.lyt.framework.bean.ResultData;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/questionTarget")
public interface QuestionTargetController {

    /**
     * 根据 指标ID 获取指标详情
     */
    @PostMapping("/getById")
    ResultData getById(@RequestBody RequestData data);

    /**
     * 根据 一级指标ID 获取二级指标列表
     */
    @PostMapping("/getList")
    ResultData getList(@RequestBody RequestData data);

    /**
     * 指标 添加、编辑
     */
    @PostMapping("/save")
    ResultData save(@RequestBody RequestData data);
}
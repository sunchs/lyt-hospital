package com.sunchs.lyt.question.controller;

import com.sunchs.lyt.framework.bean.RequestData;
import com.sunchs.lyt.framework.bean.ResultData;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/questionAttribute")
public interface QuestionAttributeController {

    /**
     * 根据 属性ID 获取属性详情
     */
    @PostMapping("/getById")
    ResultData getById(@RequestBody RequestData data);

    /**
     * 根据 属性ID 获取属性列表
     */
    @PostMapping("/getList")
    ResultData getList(@RequestBody RequestData data);

    /**
     * 指标 添加、编辑
     */
    @PostMapping("/save")
    ResultData save(@RequestBody RequestData data);
}

package com.sunchs.lyt.question.controller;

import com.sunchs.lyt.framework.bean.RequestData;
import com.sunchs.lyt.framework.bean.ResultData;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/questionOption")
public interface QuestionOptionController {
    /**
     * 选项 添加、编辑
     */
    @PostMapping("/save")
    ResultData save(@RequestBody RequestData data);

    /**
     * 根据 类型ID 获取选项详情
     */
    @PostMapping("/getById")
    ResultData getById(@RequestBody RequestData data);

    /**
     * 获取所有选项数据
     */
    @PostMapping("/getList")
    ResultData getList(@RequestBody RequestData data);
}

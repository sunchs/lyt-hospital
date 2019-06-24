package com.sunchs.lyt.question.controller.impl;

import com.sunchs.lyt.framework.bean.RequestData;
import com.sunchs.lyt.framework.bean.ResultData;
import com.sunchs.lyt.framework.controller.BaseController;
import com.sunchs.lyt.question.bean.QuestionTargetParam;
import com.sunchs.lyt.question.service.impl.QuestionTargetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/questionTarget")
public class QuestionTargetController extends BaseController {

    @Autowired
    QuestionTargetService questionTargetService;

    /**
     * 根据 指标ID 获取指标详情
     */
    @PostMapping("/getById")
    public ResultData getById(@RequestBody RequestData data) {
        QuestionTargetParam param = data.toObject(QuestionTargetParam.class);
        return success(questionTargetService.getById(param.getId()));
    }

//    @Override
//    public ResultData getList(@RequestBody RequestData data) {
//        QuestionTargetParam param = data.toObject(QuestionTargetParam.class);
//        return success(questionTargetService.getList(param.getId()));
//    }

    /**
     * 获取所有指标数据
     */
    @PostMapping("/getAll")
    public ResultData getAll(@RequestBody RequestData data) {
        return success(questionTargetService.getAll());
    }

    /**
     * 获取菜单数据
     */
    @PostMapping("/getCascaderData")
    public ResultData getCascaderData(RequestData data) {
        return success(questionTargetService.getCascaderData());
    }

    /**
     * 指标 添加、编辑
     */
    @PostMapping("/save")
    public ResultData save(@RequestBody RequestData data) {
        QuestionTargetParam param = data.toObject(QuestionTargetParam.class);
        questionTargetService.save(param);
        return success();
    }
}

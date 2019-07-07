package com.sunchs.lyt.question.controller;

import com.sunchs.lyt.framework.bean.RequestData;
import com.sunchs.lyt.framework.bean.ResultData;
import com.sunchs.lyt.framework.controller.BaseController;
import com.sunchs.lyt.question.bean.OptionTemplateParam;
import com.sunchs.lyt.question.bean.QuestionOptionParam;
import com.sunchs.lyt.question.service.IQuestionOptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/questionOption")
public class QuestionOptionController extends BaseController {

    @Autowired
    private IQuestionOptionService optionService;

    /**
     * 选项 添加、编辑
     */
    @PostMapping("/save")
    public ResultData save(@RequestBody RequestData data) {
        QuestionOptionParam param = data.toObject(QuestionOptionParam.class);
        optionService.save(param);
        return success();
    }

    /**
     * 选项 添加、编辑
     */
    @PostMapping("/saveTemplate")
    public ResultData saveTemplate(@RequestBody RequestData data) {
        OptionTemplateParam param = data.toObject(OptionTemplateParam.class);
        return success(optionService.saveTemplate(param));
    }

    /**
     * 根据 模版ID 获取选项模版
     */
    @PostMapping("/getTemplateById")
    public ResultData getTemplateById(@RequestBody RequestData data) {
        OptionTemplateParam param = data.toObject(OptionTemplateParam.class);
        return success(optionService.getTemplateById(param.getId()));
    }

    /**
     * 根据 类型ID 获取选项详情
     */
    @PostMapping("/getById")
    public ResultData getById(@RequestBody RequestData data) {
        QuestionOptionParam param = data.toObject(QuestionOptionParam.class);
        return success(optionService.getInfo(param));
    }

    /**
     * 获取所有选项数据
     */
    @PostMapping("/getList")
    public ResultData getList(@RequestBody RequestData data) {
        QuestionOptionParam param = data.toObject(QuestionOptionParam.class);
        return success(optionService.getList(param));
    }

    /**
     * 获取 级联 数据
     */
    @PostMapping("/getCascaderData")
    public ResultData getCascaderData() {
        return success(optionService.getCascaderData());
    }
}
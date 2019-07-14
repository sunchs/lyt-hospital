package com.sunchs.lyt.question.controller;

import com.sunchs.lyt.framework.bean.RequestData;
import com.sunchs.lyt.framework.bean.ResultData;
import com.sunchs.lyt.framework.controller.BaseController;
import com.sunchs.lyt.question.bean.OptionTemplateParam;
import com.sunchs.lyt.question.bean.QuestionTagParam;
import com.sunchs.lyt.question.service.impl.QuestionTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/questionTag")
public class QuestionTagController extends BaseController {

    @Autowired
    QuestionTagService tagService;

    /**
     * 根据 属性ID 获取属性详情
     */
    @PostMapping("/getById")
    public ResultData getById(@RequestBody RequestData data) {
        QuestionTagParam param = data.toObject(QuestionTagParam.class);
        return success(tagService.getById(param.getId()));
    }

    /**
     * 根据 属性ID 获取属性列表
     */
    @PostMapping("/getList")
    public ResultData getList(@RequestBody RequestData data) {
        QuestionTagParam param = data.toObject(QuestionTagParam.class);
        return success(tagService.getList(param.getId()));
    }

    /**
     * 指标 添加、编辑
     */
    @PostMapping("/save")
    public ResultData save(@RequestBody RequestData data) {
        QuestionTagParam param = data.toObject(QuestionTagParam.class);
        return success(tagService.save(param));
    }

    /**
     * 根据 模版ID 更新模版状态
     */
    @PostMapping("/updateStatus")
    public ResultData updateStatus(@RequestBody RequestData data) {
        QuestionTagParam param = data.toObject(QuestionTagParam.class);
        optionService.updateStatus(param);
        return success();
    }

    /**
     * 获取 级联 数据
     */
    @PostMapping("/getCascaderData")
    public ResultData getCascaderData() {
        return success(tagService.getCascaderData());
    }
}

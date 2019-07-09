package com.sunchs.lyt.question.service;

import com.sunchs.lyt.question.bean.OptionTemplateData;
import com.sunchs.lyt.question.bean.OptionTemplateParam;
import com.sunchs.lyt.question.bean.QuestionOptionData;
import com.sunchs.lyt.question.bean.QuestionOptionParam;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface IQuestionOptionService {

    /**
     * 保存数据
     */
    void save(QuestionOptionParam param);

    /**
     * 添加选项模版
     */
    int saveTemplate(OptionTemplateParam param);

    /**
     * 根据 模版ID 获取选项模版
     */
    OptionTemplateData getTemplateById(int templateId);

    /**
     * 根据 模版ID 更新模版状态
     */
    void updateTemplateStatus(OptionTemplateParam param);


    QuestionOptionData getInfo(QuestionOptionParam param);


    List<QuestionOptionData> getList(QuestionOptionParam param);

    /**
     * 获取 级联 数据
     */
    List<Map<String, Object>> getCascaderData();

}

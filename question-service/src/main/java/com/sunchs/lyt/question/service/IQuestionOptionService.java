package com.sunchs.lyt.question.service;

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


    QuestionOptionData getInfo(QuestionOptionParam param);


    List<QuestionOptionData> getList(QuestionOptionParam param);

    /**
     * 获取 级联 数据
     */
    List<Map<String, Object>> getCascaderData();

}

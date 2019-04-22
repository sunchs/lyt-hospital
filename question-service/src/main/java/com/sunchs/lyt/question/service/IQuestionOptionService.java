package com.sunchs.lyt.question.service;

import com.sunchs.lyt.question.bean.QuestionOptionData;
import com.sunchs.lyt.question.bean.QuestionOptionParam;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IQuestionOptionService {

    /**
     * 保存数据
     */
    QuestionOptionData save(QuestionOptionParam param);


    QuestionOptionData getInfo(QuestionOptionParam param);


    List<QuestionOptionData> getList(QuestionOptionParam param);

}

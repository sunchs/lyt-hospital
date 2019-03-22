package com.sunchs.lyt.question.service;

import com.sunchs.lyt.question.bean.QuestionData;
import com.sunchs.lyt.question.bean.QuestionParam;

public interface QuestionService {

    /**
     * 保存数据
     */
    QuestionData save(QuestionParam param);

}

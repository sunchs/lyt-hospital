package com.sunchs.lyt.question.dao;

import com.sunchs.lyt.question.bean.OptionBean;
import com.sunchs.lyt.question.bean.OptionData;
import com.sunchs.lyt.question.bean.QuestionOptionData;

import java.util.List;
import java.util.Map;

public interface QuestionOptionDao {

    /**
     * 根据 问题ID 获取选项列表
     * @param questionId
     */
    List<OptionData> getListById(int questionId);

    QuestionOptionData getInfo(int id);

    List<OptionBean> getOptionList(int id);

    List<QuestionOptionData> getTypeList();

    OptionBean getOption(int id);

    // 更新
    int update(Map<String, Object> param);

    void insertOption(int typeId, String content);

    void deleteOption(int id);
}

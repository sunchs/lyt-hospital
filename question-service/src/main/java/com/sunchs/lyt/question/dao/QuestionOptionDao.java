package com.sunchs.lyt.question.dao;

import com.sunchs.lyt.db.business.entity.OptionTemplate;
import com.sunchs.lyt.db.business.entity.QuestionOption;
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

    /**
     * 根据 选项模版ID 获取选项数据
     */
    OptionTemplate getOptionById(int optionId);

    /**
     * 添加问题选项
     */
    boolean insertQuestionOption(QuestionOption questionOption);

    boolean update(OptionTemplate optionTemplate);

    void insertOption(int typeId, String content);

    void deleteOption(int id);

}

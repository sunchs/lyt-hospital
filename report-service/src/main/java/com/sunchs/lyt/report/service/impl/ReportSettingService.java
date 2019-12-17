package com.sunchs.lyt.report.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.sunchs.lyt.db.business.entity.Questionnaire;
import com.sunchs.lyt.db.business.entity.ReportAnswer;
import com.sunchs.lyt.db.business.entity.ReportAnswerOption;
import com.sunchs.lyt.db.business.service.impl.QuestionnaireServiceImpl;
import com.sunchs.lyt.db.business.service.impl.ReportAnswerOptionServiceImpl;
import com.sunchs.lyt.db.business.service.impl.ReportAnswerServiceImpl;
import com.sunchs.lyt.framework.bean.SelectChildData;
import com.sunchs.lyt.framework.bean.TitleData;
import com.sunchs.lyt.framework.enums.OfficeTypeEnum;
import com.sunchs.lyt.report.service.IReportSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReportSettingService implements IReportSettingService {

    @Autowired
    private ReportAnswerServiceImpl reportAnswerService;

    @Autowired
    private ReportAnswerOptionServiceImpl reportAnswerOptionService;

    @Autowired
    private QuestionnaireServiceImpl questionnaireService;

    @Override
    public List<TitleData> getItemUseQuestionnaireList(Integer itemId) {
        List<TitleData> result = new ArrayList<>();
        Wrapper<ReportAnswer> wrapper = new EntityWrapper<ReportAnswer>()
                .eq(ReportAnswer.ITEM_ID, itemId)
                .groupBy(ReportAnswer.QUESTIONNAIRE_ID);
        List<ReportAnswer> answerList = reportAnswerService.selectList(wrapper);
        answerList.forEach(a->{
            Questionnaire q = getQuestionnaireById(a.getQuestionnaireId());
            if (Objects.nonNull(q)) {
                TitleData data = new TitleData();
                data.setId(a.getQuestionnaireId());
                data.setType(q.getTargetOne());
                data.setTitle(q.getTitle());
                data.setSelected(false);
                result.add(data);
            }
        });
        return result;
    }

    @Override
    public List<Map<String, Object>> getItemUseAllList(Integer itemId) {
        List<Map<String, Object>> result = new ArrayList<>();
        List<TitleData> itemUseQuestionnaireList = getItemUseQuestionnaireList(itemId);
        Map<Integer, List<TitleData>> groupList = itemUseQuestionnaireList.stream().collect(Collectors.groupingBy(TitleData::getType));
        for (Integer type : groupList.keySet()) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", type);
            map.put("title", OfficeTypeEnum.get(type));
            // 获取列表
            List<TitleData> sonList = groupList.get(type);
            map.put("children", getQuestionList(itemId, sonList));
            result.add(map);
        }
        return result;
    }

    /**
     * 获取问卷名称
     */
    private Questionnaire getQuestionnaireById(Integer questionnaireId) {
        Questionnaire row = questionnaireService.selectById(questionnaireId);
        if (Objects.nonNull(row)) {
            return row;
        }
        return null;
    }

    private List<Map<String, Object>> getQuestionList(Integer itemId, List<TitleData> list) {
        List<Map<String, Object>> result = new ArrayList<>();
        list.forEach(wj->{
            Map<String, Object> map = new HashMap<>();
            map.put("id", wj.getId());
            map.put("title", wj.getTitle());
            Wrapper<ReportAnswerOption> wrapper = new EntityWrapper<ReportAnswerOption>()
                    .eq(ReportAnswerOption.ITEM_ID, itemId)
                    .eq(ReportAnswerOption.QUESTIONNAIRE_ID, wj.getId())
                    .groupBy(ReportAnswerOption.QUESTION_ID);
            List<ReportAnswerOption> answerOptionList = reportAnswerOptionService.selectList(wrapper);
            map.put("children", getOptionList(itemId, answerOptionList));
            result.add(map);
        });
        return result;
    }

    private List<Map<String, Object>> getOptionList(Integer itemId, List<ReportAnswerOption> answerOptionList) {
        List<Map<String, Object>> result = new ArrayList<>();
        answerOptionList.forEach(a->{
            Map<String, Object> map = new HashMap<>();
            map.put("id", a.getQuestionId());
            map.put("title", a.getQuestionName());

            List<Map<String, Object>> oList = new ArrayList<>();
            Wrapper<ReportAnswerOption> wrapper = new EntityWrapper<ReportAnswerOption>()
                    .eq(ReportAnswerOption.ITEM_ID, itemId)
                    .eq(ReportAnswerOption.QUESTIONNAIRE_ID, a.getQuestionnaireId())
                    .eq(ReportAnswerOption.QUESTION_ID, a.getQuestionId())
                    .groupBy(ReportAnswerOption.OPTION_ID);
            List<ReportAnswerOption> optionList = reportAnswerOptionService.selectList(wrapper);
            optionList.forEach(b->{
                Map<String, Object> m = new HashMap<>();
                m.put("id", b.getOptionId());
                m.put("title", b.getOptionName());
                oList.add(m);
            });
            map.put("children", oList);
            result.add(map);
        });
        return result;
    }
}

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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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
    public List<SelectChildData> getItemUseAllList(Integer itemId) {
        List<SelectChildData> result = new ArrayList<>();
        List<TitleData> itemUseQuestionnaireList = getItemUseQuestionnaireList(itemId);
        Map<Integer, List<TitleData>> groupList = itemUseQuestionnaireList.stream().collect(Collectors.groupingBy(TitleData::getType));
        for (Integer type : groupList.keySet()) {
            SelectChildData pData = new SelectChildData();
            pData.setId(type);
            pData.setPid(0);
            pData.setTitle(OfficeTypeEnum.get(type));
            pData.setSelected(false);
            // 获取列表
            List<TitleData> sonList = groupList.get(type);
            pData.setChildren(getQuestionList(itemId, sonList));
            result.add(pData);
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

    private List<SelectChildData> getQuestionList(Integer itemId, List<TitleData> list) {
        List<SelectChildData> result = new ArrayList<>();
        list.forEach(wj->{
            SelectChildData pData = new SelectChildData();
            pData.setId(wj.getId());
            pData.setPid(wj.getType());
            pData.setTitle(wj.getTitle());
            pData.setSelected(false);

            Wrapper<ReportAnswerOption> wrapper = new EntityWrapper<ReportAnswerOption>()
                    .eq(ReportAnswerOption.ITEM_ID, itemId)
                    .eq(ReportAnswerOption.QUESTIONNAIRE_ID, wj.getId())
                    .groupBy(ReportAnswerOption.QUESTION_ID);
            List<ReportAnswerOption> answerOptionList = reportAnswerOptionService.selectList(wrapper);
            pData.setChildren(getOptionList(itemId, answerOptionList));
        });
        return result;
    }

    private List<SelectChildData> getOptionList(Integer itemId, List<ReportAnswerOption> answerOptionList) {
        List<SelectChildData> result = new ArrayList<>();
        answerOptionList.forEach(a->{
            SelectChildData pData = new SelectChildData();
            pData.setId(a.getQuestionId());
            pData.setPid(a.getQuestionnaireId());
            pData.setTitle(a.getQuestionName());
            pData.setSelected(false);

            List<SelectChildData> oList = new ArrayList<>();
            Wrapper<ReportAnswerOption> wrapper = new EntityWrapper<ReportAnswerOption>()
                    .eq(ReportAnswerOption.ITEM_ID, itemId)
                    .eq(ReportAnswerOption.QUESTIONNAIRE_ID, a.getQuestionnaireId())
                    .eq(ReportAnswerOption.QUESTION_ID, a.getQuestionId())
                    .groupBy(ReportAnswerOption.OPTION_ID);
            List<ReportAnswerOption> optionList = reportAnswerOptionService.selectList(wrapper);
            optionList.forEach(b->{
                SelectChildData oSon = new SelectChildData();
                oSon.setId(b.getOptionId());
                oSon.setPid(b.getQuestionId());
                oSon.setTitle(b.getOptionName());
                oSon.setSelected(false);
                oSon.setChildren(new ArrayList<>());
                oList.add(oSon);
            });
            pData.setChildren(oList);
            result.add(pData);
        });
        return result;
    }
}

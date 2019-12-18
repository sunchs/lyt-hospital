package com.sunchs.lyt.report.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.sunchs.lyt.db.business.entity.*;
import com.sunchs.lyt.db.business.service.impl.*;
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

    @Autowired
    private ReportAnswerQuantityServiceImpl reportAnswerQuantityService;

    @Autowired
    private QuestionTargetServiceImpl questionTargetService;

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

    @Override
    public List<Map<String, Object>> getItemQuestionnaireUseTarget(Integer itemId, Integer questionnaireId) {
        List<Map<String, Object>> result = new ArrayList<>();
        Wrapper<ReportAnswerQuantity> wrapper = new EntityWrapper<ReportAnswerQuantity>()
                .eq(ReportAnswerQuantity.ITEM_ID, itemId)
                .eq(ReportAnswerQuantity.QUESTIONNAIRE_ID, questionnaireId);
        List<ReportAnswerQuantity> list = reportAnswerQuantityService.selectList(wrapper);
        Set<Integer> targetIds = new HashSet<>();
        list.forEach(t->{
            targetIds.add(t.getTargetOne());
            targetIds.add(t.getTargetTwo());
            targetIds.add(t.getTargetThree());
        });
        Wrapper<QuestionTarget> targetWrapper = new EntityWrapper<QuestionTarget>()
                .setSqlSelect(QuestionTarget.ID, QuestionTarget.TITLE)
                .in(QuestionTarget.ID, targetIds);
        List<QuestionTarget> targetList = questionTargetService.selectList(targetWrapper);
        Map<Integer, String> targetMap = targetList.stream().collect(Collectors.toMap(QuestionTarget::getId, QuestionTarget::getTitle));

        Map<Integer, List<ReportAnswerQuantity>> oneList = list.stream().collect(Collectors.groupingBy(ReportAnswerQuantity::getTargetOne));
        for (Integer oneId : oneList.keySet()) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", oneId);
            map.put("title", targetMap.get(oneId));
            List<ReportAnswerQuantity> twoTempList = oneList.get(oneId);
            if (Objects.nonNull(twoTempList) && twoTempList.size() > 0) {
                map.put("children", getTwoTarget(twoTempList, targetMap));
            }
            result.add(map);
        }
        return result;
    }

    private List<Map<String, Object>> getTwoTarget(List<ReportAnswerQuantity> twoTempList, Map<Integer, String> targetMap) {
        List<Map<String, Object>> result = new ArrayList<>();
        Map<Integer, List<ReportAnswerQuantity>> twoList = twoTempList.stream().collect(Collectors.groupingBy(ReportAnswerQuantity::getTargetTwo));
        for (Integer towId : twoList.keySet()) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", towId);
            map.put("title", targetMap.get(towId));
            List<ReportAnswerQuantity> threeList = twoList.get(towId);
            if (Objects.nonNull(threeList) && threeList.size() > 0) {
                map.put("children", getThreeTarget(threeList, targetMap));
            }
            result.add(map);
        }
        return result;
    }

    private List<Map<String, Object>> getThreeTarget(List<ReportAnswerQuantity> list, Map<Integer, String> targetMap) {
        List<Map<String, Object>> result = new ArrayList<>();
        list.forEach(t->{
            Map<String, Object> map = new HashMap<>();
            map.put("id", t.getTargetThree());
            map.put("title", targetMap.get(t.getTargetThree()));
            result.add(map);
        });
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

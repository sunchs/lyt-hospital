package com.sunchs.lyt.report.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.sunchs.lyt.db.business.entity.QuestionTarget;
import com.sunchs.lyt.db.business.entity.Questionnaire;
import com.sunchs.lyt.db.business.entity.ReportAnswerOption;
import com.sunchs.lyt.db.business.service.impl.QuestionTargetServiceImpl;
import com.sunchs.lyt.db.business.service.impl.QuestionnaireServiceImpl;
import com.sunchs.lyt.db.business.service.impl.ReportAnswerOptionServiceImpl;
import com.sunchs.lyt.framework.bean.IdTitleData;
import com.sunchs.lyt.report.bean.ItemCompareValue;
import com.sunchs.lyt.report.bean.ItemRelatedData;
import com.sunchs.lyt.report.service.IReportRelatedService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReportRelatedService implements IReportRelatedService {

    @Autowired
    private ReportAnswerOptionServiceImpl reportAnswerOptionService;

    @Autowired
    private QuestionTargetServiceImpl questionTargetService;

    @Autowired
    private QuestionnaireServiceImpl questionnaireService;

    @Override
    public ItemRelatedData getItemRelatedData(Integer itemId, Integer officeType) {
        ItemRelatedData data = new ItemRelatedData();
        // 检查是否有多问卷
        Wrapper<ReportAnswerOption> checkWrapper = new EntityWrapper<ReportAnswerOption>()
                .setSqlSelect(ReportAnswerOption.QUESTIONNAIRE_ID.concat(" AS questionnaireId"))
                .eq(ReportAnswerOption.ITEM_ID, itemId)
                .eq(ReportAnswerOption.OFFICE_TYPE_ID, officeType)
                .in(ReportAnswerOption.OPTION_TYPE, Arrays.asList(1, 4))
                .groupBy(ReportAnswerOption.QUESTIONNAIRE_ID);
        List<ReportAnswerOption> checkList = reportAnswerOptionService.selectList(checkWrapper);
        if (checkList.size() == 0) {
            data.setColList(new ArrayList<>());
            data.setRowList(new ArrayList<>());
            data.setValueList(new ArrayList<>());
            return data;
        }
        int questionnairId = 0;
        List<Integer> questionnaireList = checkList.stream().map(ReportAnswerOption::getQuestionnaireId).collect(Collectors.toList());
        if (questionnaireList.size() > 1) {
            Wrapper<Questionnaire> questionnaireWrapper = new EntityWrapper<Questionnaire>()
                    .setSqlSelect(
                            Questionnaire.ID,
                            Questionnaire.TITLE
                    )
                    .in(Questionnaire.ID, questionnaireList);
            List<Questionnaire> questionnaires = questionnaireService.selectList(questionnaireWrapper);
            for (Questionnaire q : questionnaires) {
                String title = q.getTitle();
                if (title.indexOf("（门诊）") != -1) {
                    questionnairId = q.getId();
                    break;
                }
                if (title.indexOf("(门诊)") != -1) {
                    questionnairId = q.getId();
                    break;
                }
            }
        }
        if (questionnairId == 0) {
            questionnairId = questionnaireList.get(0);
        }

        // 获取答卷数据
        Wrapper<ReportAnswerOption> wrapper = new EntityWrapper<ReportAnswerOption>()
                .setSqlSelect(
                        ReportAnswerOption.ID,
                        ReportAnswerOption.ANSWER_ID.concat(" AS answerId"),
                        ReportAnswerOption.TARGET_THREE.concat(" AS targetThree"),
                        ReportAnswerOption.OPTION_ID.concat(" AS optionId"),
                        ReportAnswerOption.SCORE
                )
                .eq(ReportAnswerOption.ITEM_ID, itemId)
                .eq(ReportAnswerOption.OFFICE_TYPE_ID, officeType)
                .in(ReportAnswerOption.OPTION_TYPE, Arrays.asList(1, 4))
                .eq(ReportAnswerOption.QUESTIONNAIRE_ID, questionnairId);
        List<ReportAnswerOption> optionList = reportAnswerOptionService.selectList(wrapper);
//        Map<Integer, List<ReportAnswerOption>> optionGroupMap = optionList.stream().collect(Collectors.groupingBy(ReportAnswerOption::getTargetThree));


        // 按照答卷分组
        Map<Integer, List<ReportAnswerOption>> answerTempList = optionList.stream().collect(Collectors.groupingBy(ReportAnswerOption::getAnswerId));
        // 设计的指标ID
        Set<Integer> targetIds = optionList.stream().map(ReportAnswerOption::getTargetThree).distinct().collect(Collectors.toSet());
        // 指标标题
        Map<Integer, String> targetNameMap = getTargetNameByIds(targetIds);

        System.out.println("总共："+answerTempList.size());

        // 表头
        List<IdTitleData> colList = new ArrayList<>();
        List<IdTitleData> rowList = new ArrayList<>();
        for (Integer targetId : targetIds) {
            IdTitleData row = new IdTitleData();
            row.setId(targetId);
            row.setTitle(targetNameMap.get(targetId));
            colList.add(row);
            rowList.add(row);
        }
        data.setColList(colList);
        data.setRowList(rowList);

        // 计算
        List<ItemCompareValue> valueList = new ArrayList<>();
        for (Integer targetId : targetIds) {
            for (Integer tId : targetIds) {

                ItemCompareValue row = new ItemCompareValue();
                row.setColId(targetId);
                row.setRowId(tId);

                // 求相关系数
                Map<Integer, Double> mapX = new HashMap<>();
                Map<Integer, Double> mapY = new HashMap<>();
                // 求X轴数据
                for (Integer index : answerTempList.keySet()) {
                    List<ReportAnswerOption> value = answerTempList.get(index);
                    Optional<ReportAnswerOption> firstRow = value.stream().filter(v -> v.getTargetThree().equals(targetId)).findFirst();
                    if (firstRow.isPresent()) {
                        Integer score = firstRow.get().getScore();

                        if (score.equals(100)) {
                            mapX.put(index, 5d);
                        } else if (score.equals(80)) {
                            mapX.put(index, 4d);
                        } else if (score.equals(60)) {
                            mapX.put(index, 3d);
                        } else if (score.equals(40)) {
                            mapX.put(index, 2d);
                        } else if (score.equals(20)) {
                            mapX.put(index, 1d);
                        } else {
//                            mapX.put(index, 0.00);
                        }
//                        mapX.put(index, firstRow.get().getOptionId().doubleValue());
                    } else {
                        mapX.put(index, 0.00);
//                        mapX.put(index, null);
                    }
                }
                // 求Y轴数据
                for (Integer index : answerTempList.keySet()) {
                    List<ReportAnswerOption> value = answerTempList.get(index);
                    Optional<ReportAnswerOption> firstRow = value.stream().filter(v -> v.getTargetThree().equals(tId)).findFirst();
                    if (firstRow.isPresent()) {
                        Integer score = firstRow.get().getScore();
                        if (score.equals(100)) {
                            mapY.put(index, 5d);
                        } else if (score.equals(80)) {
                            mapY.put(index, 4d);
                        } else if (score.equals(60)) {
                            mapY.put(index, 3d);
                        } else if (score.equals(40)) {
                            mapY.put(index, 2d);
                        } else if (score.equals(20)) {
                            mapY.put(index, 1d);
                        } else {
//                            mapY.put(index, 0.00);
                        }

//                        mapY.put(index, firstRow.get().getOptionId().doubleValue());
                    } else {
                        mapY.put(index, 0.00);
//                        mapY.put(index, null);
                    }
                }

                for (Integer key : mapY.keySet()) {
                    if (mapY.values().equals(0)) {
                        mapX.remove(key);
                    }
                }

                for (Integer key : mapX.keySet()) {
                    if (mapX.values().equals(0)) {
                        mapY.remove(key);
                    }
                }

                if (mapX.size() != mapY.size()) {
                    System.out.println("数量不相等"+mapX.size()+"："+mapY.size());
                }
                double value = caculatePearson(mapX, mapY);
                if (value == -1) {
                    System.out.println("--------------------------");
                    for (Integer i : mapX.keySet()) {
                        System.out.println(mapX.get(i)+"\t"+mapY.get(i));
                    }
                    System.out.println("==================================");
                }
                row.setValue(value);
                valueList.add(row);
            }
        }
        data.setValueList(valueList);


//        // 表头
//        List<IdTitleData> colList = new ArrayList<>();
//        List<IdTitleData> rowList = new ArrayList<>();
//        for (Integer targetId : optionGroupMap.keySet()) {
//            IdTitleData row = new IdTitleData();
//            row.setId(targetId);
//            row.setTitle(targetNameMap.get(targetId));
//            colList.add(row);
//            rowList.add(row);
//        }
//        data.setColList(colList);
//        data.setRowList(rowList);
//
//        // 计算
//        List<ItemCompareValue> valueList = new ArrayList<>();
//        for (Integer targetId : optionGroupMap.keySet()) {
//            for (Integer tId : optionGroupMap.keySet()) {
//
//                ItemCompareValue row = new ItemCompareValue();
//                row.setRowId(tId);
//                row.setColId(targetId);
//                // 求相关系数
//                Map<Integer, Double> mapX = new HashMap<>();
//                Map<Integer, Double> mapY = new HashMap<>();
//
//                List<ReportAnswerOption> oneAnswerOptionList = optionGroupMap.get(targetId);
//                if (CollectionUtils.isEmpty(oneAnswerOptionList)) {
//                    System.out.println("指标无数据");
//                }
//                for (int i = 0; i < oneAnswerOptionList.size(); i++) {
//                    mapX.put(i, oneAnswerOptionList.get(i).getOptionId().doubleValue());
//                }
//
//                List<ReportAnswerOption> twoAnswerOptionList = optionGroupMap.get(tId);
//                for (int i = 0; i < twoAnswerOptionList.size(); i++) {
//                    mapY.put(i, twoAnswerOptionList.get(i).getOptionId().doubleValue());
//                }
//
////                optionGroupMap.get(targetId).forEach(t1->{
////                    mapX.put(t1.getId(), t1.getOptionId().doubleValue());
////                });
////                optionGroupMap.get(tId).forEach(t2->{
////                    mapY.put(t2.getId(), t2.getOptionId().doubleValue());
////                });
//                if (mapX.size() != mapY.size()) {
//                    System.out.println("数量不相等"+mapX.size()+"："+mapY.size());
//                }
//                double value = caculatePearson(mapX, mapY);
//                row.setValue(value);
//                valueList.add(row);
//            }
//        }
//        data.setValueList(valueList);
        return data;
    }

    /**
     * 根据 指标ID集合 获取指标名称
     */
    private Map<Integer, String> getTargetNameByIds(Set<Integer> targetIds) {
        Wrapper<QuestionTarget> wrapper = new EntityWrapper<QuestionTarget>()
                .setSqlSelect(QuestionTarget.ID, QuestionTarget.TITLE)
                .in(QuestionTarget.ID, targetIds);
        List<QuestionTarget> itemList = questionTargetService.selectList(wrapper);
        return itemList.stream().collect(Collectors.toMap(QuestionTarget::getId, QuestionTarget::getTitle));
    }

    /**
     * 求相关系数
     */
    private double caculatePearson(Map<Integer, Double> mapX, Map<Integer, Double> mapY) {
        double sumXY = 0d;
        double sumX = 0d;
        double sumY = 0d;
        double sumPowX = 0d;
        double sumPowY = 0d;
        Set<Integer> setItem = new HashSet<>();
        for (Map.Entry<Integer, Double> entry : mapX.entrySet()) {
            setItem.add(entry.getKey());
        }
        for (Map.Entry<Integer, Double> entry : mapY.entrySet()) {
            setItem.add(entry.getKey());
        }
        for (Integer bookId : setItem) {
            Double x = mapX.get(bookId);
            if (x == null) {
                x = 0d;
            }
            Double y = mapY.get(bookId);
            if (y == null) {
                y = 0d;
            }
            sumXY += x * y;
            sumX += x;
            sumY += y;
            sumPowX += Math.pow(x, 2);
            sumPowY += Math.pow(y, 2);
        }
        int n = setItem.size();
        double pearson = (sumXY - sumX * sumY / n) / Math.sqrt((sumPowX - Math.pow(sumX, 2) / n) * (sumPowY - Math.pow(sumY, 2) / n));
        double value = new BigDecimal(pearson).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        return value;
    }
}

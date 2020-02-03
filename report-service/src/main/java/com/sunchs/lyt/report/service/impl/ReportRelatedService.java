package com.sunchs.lyt.report.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.sunchs.lyt.db.business.entity.QuestionTarget;
import com.sunchs.lyt.db.business.entity.ReportAnswerOption;
import com.sunchs.lyt.db.business.service.impl.QuestionTargetServiceImpl;
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

    @Override
    public ItemRelatedData getItemRelatedData(Integer itemId, Integer officeType) {
        ItemRelatedData data = new ItemRelatedData();
        Wrapper<ReportAnswerOption> wrapper = new EntityWrapper<ReportAnswerOption>()
                .setSqlSelect(
                        ReportAnswerOption.ID,
                        ReportAnswerOption.ANSWER_ID.concat(" AS answerId"),
                        ReportAnswerOption.TARGET_THREE.concat(" AS targetThree"),
                        ReportAnswerOption.OPTION_ID.concat(" AS optionId")
                )
                .eq(ReportAnswerOption.ITEM_ID, itemId)
                .eq(ReportAnswerOption.OFFICE_TYPE_ID, officeType)
                .in(ReportAnswerOption.OPTION_TYPE, Arrays.asList(1, 4));
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
                        mapX.put(index, firstRow.get().getOptionId().doubleValue());
                    } else {
                        mapX.put(index, 0.00);
                    }
                }
                // 求Y轴数据
                for (Integer index : answerTempList.keySet()) {
                    List<ReportAnswerOption> value = answerTempList.get(index);
                    Optional<ReportAnswerOption> firstRow = value.stream().filter(v -> v.getTargetThree().equals(tId)).findFirst();
                    if (firstRow.isPresent()) {
                        mapY.put(index, firstRow.get().getOptionId().doubleValue());
                    } else {
                        mapY.put(index, 0.00);
                    }
                }

                if (mapX.size() != mapY.size()) {
                    System.out.println("数量不相等"+mapX.size()+"："+mapY.size());
                }
                double value = caculatePearson(mapX, mapY);
                if (value == -1) {
                    System.out.println("--------------------------");
                    System.out.println("X:::"+mapX);
                    System.out.println("Y:::"+mapY);
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

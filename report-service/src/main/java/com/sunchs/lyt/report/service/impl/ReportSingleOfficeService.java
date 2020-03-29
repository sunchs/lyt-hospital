package com.sunchs.lyt.report.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.sunchs.lyt.db.business.entity.*;
import com.sunchs.lyt.db.business.service.impl.*;
import com.sunchs.lyt.framework.bean.TitleValueChildrenData;
import com.sunchs.lyt.framework.bean.TitleValueData;
import com.sunchs.lyt.framework.util.NumberUtil;
import com.sunchs.lyt.report.bean.*;
import com.sunchs.lyt.report.exception.ReportException;
import com.sunchs.lyt.report.service.IReportSingleOfficeService;
import jxl.Workbook;
import jxl.format.Colour;
import jxl.write.Label;
import jxl.write.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReportSingleOfficeService implements IReportSingleOfficeService {

    @Autowired
    private ReportAnswerOptionServiceImpl reportAnswerOptionService;

    @Autowired
    private ReportItemScoreServiceImpl reportItemScoreService;

    @Autowired
    private QuestionServiceImpl questionService;

    @Autowired
    private ReportAnswerServiceImpl reportAnswerService;

    @Autowired
    private ItemOfficeServiceImpl itemOfficeService;

    @Autowired
    private ReportSettingService reportSettingService;

    @Autowired
    private ReportAnswerQuantityServiceImpl reportAnswerQuantityService;

    @Override
    public SingleOfficeSatisfyData getItemSingleOfficeSatisfy(Integer itemId, Integer officeType, Integer officeId) {
        Wrapper<ReportAnswerOption> wrapper = new EntityWrapper<ReportAnswerOption>()
                .setSqlSelect(
                        ReportAnswerOption.ANSWER_ID.concat(" as answerId"),
                        ReportAnswerOption.OFFICE_TYPE_ID.concat(" as officeTypeId"),
                        ReportAnswerOption.OFFICE_ID.concat(" as officeId"),
                        ReportAnswerOption.TARGET_THREE.concat(" as targetThree"),
                        ReportAnswerOption.QUESTION_ID.concat(" as questionId"),
                        ReportAnswerOption.OPTION_ID.concat(" as optionId"),
                        ReportAnswerOption.SCORE,
                        "COUNT(1) as quantity"
                )
                .eq(ReportAnswerOption.ITEM_ID, itemId)
                .eq(ReportAnswerOption.OFFICE_TYPE_ID, officeType)
                .in(ReportAnswerOption.OPTION_TYPE, Arrays.asList(1, 4))
                .ne(ReportAnswerOption.SCORE, 0)
                .groupBy(ReportAnswerOption.OFFICE_TYPE_ID)
                .groupBy(ReportAnswerOption.OFFICE_ID)
                .groupBy(ReportAnswerOption.QUESTION_ID)
                .groupBy(ReportAnswerOption.OPTION_ID);
        List<ReportAnswerOption> optionList = reportAnswerOptionService.selectList(wrapper);
        // 算出 当前科室 的信息（包含排名）
        CurrentOfficeBean info = getCurrentOfficeInfo(optionList, officeId);
        // 算出 所有科室 题目相关信息
        List<TitleValueDataVO> itemTargetThreeScore = getItemQuestionScore(optionList);
        // 算出 当前科室 题目相关信息
        List<SingleOfficeData> currentQuestionScore = getCurrentQuestionScore(optionList, officeId);
        currentQuestionScore.forEach(cur -> {
            Optional<TitleValueDataVO> first = itemTargetThreeScore.stream().filter(v -> v.getId().equals(cur.getQuestionId())).findFirst();
            if (first.isPresent()) {
                TitleValueDataVO valueDataVO = first.get();
                cur.setHospitalSatisfyValue(valueDataVO.getValue());
                cur.setQuestionLevel(valueDataVO.getRankValue());
            }
        });
        // 抽样量
        Wrapper<ReportAnswer> reportAnswerWrapper = new EntityWrapper<ReportAnswer>()
                .eq(ReportAnswer.ITEM_ID, itemId)
                .eq(ReportAnswer.OFFICE_TYPE_ID, officeType)
                .eq(ReportAnswer.OFFICE_ID, officeId);
        int answerQuantity = reportAnswerService.selectCount(reportAnswerWrapper);
        // 返回结果
        SingleOfficeSatisfyData data = new SingleOfficeSatisfyData();
        data.setOfficeSatisfyValue(info.getSatisfyValue());
        data.setAnswerQuantity(answerQuantity);
        data.setLevelValue(info.getLevel());
        data.setQuestionList(currentQuestionScore);

        // 科室名称
        Wrapper<ItemOffice> itemOfficeWrapper = new EntityWrapper<ItemOffice>()
                .setSqlSelect(ItemOffice.GROUP_NAME.concat(" as groupName"), ItemOffice.TITLE, ItemOffice.OFFICE_ID.concat(" as officeId"))
                .eq(ItemOffice.ITEM_ID, itemId)
                .eq(ItemOffice.OFFICE_TYPE_ID, officeType)
                .eq(ReportAnswer.OFFICE_ID, officeId);
        ItemOffice itemOffice = itemOfficeService.selectOne(itemOfficeWrapper);
        if (Objects.nonNull(itemOffice)) {
            String officeName = itemOffice.getTitle().length()>0 ? itemOffice.getTitle() : itemOffice.getGroupName();
            data.setOfficeName(officeName);
        }
        return data;
    }

    @Override
    public SingleOfficeSatisfyData getItemSingleOfficeSatisfyV2(Integer itemId, Integer officeType, Integer officeId) {
        SingleOfficeSatisfyData res = new SingleOfficeSatisfyData();
        // 当前科室名称
        String curOfficeName = getItemOfficeNameByOfficeId(itemId, officeType, officeId);
        res.setOfficeName(curOfficeName);
//        res.setQuestionList();
        // 获取所有科室配置信息
        List<ItemTempOffice> settingList = reportSettingService.getItemTempOfficeSettingList(itemId, 0);
        // 获取全院各科室满意度
        settingList.forEach(setting -> {
            if (setting.getOfficeId() > 0 && CollectionUtils.isNotEmpty(setting.getTargetList())) {
                ReportAnswerQuantity satisfy = reportAnswerQuantityService
                        .getItemOfficeSatisfyInfo(itemId, setting.getOfficeType(), setting.getOfficeId(), setting.getTargetList());
                if (Objects.nonNull(satisfy)) {
                    setting.setSatisfyValue(satisfy.getSatisfyValue());
                    // 设置当前科室满意度、抽样量
                    if (setting.getOfficeId().equals(officeId) && setting.getOfficeType().equals(officeType)) {
                        res.setOfficeSatisfyValue(satisfy.getSatisfyValue());
                        // 题目列表
                        List<SingleOfficeData> satisfyQuestionList = getSatisfyQuestionList(itemId, officeType, officeId, setting.getTargetList());
                        res.setQuestionList(satisfyQuestionList);
                    }
                }
            }
        });
        // 抽样量
        Wrapper<ReportAnswer> reportAnswerWrapper = new EntityWrapper<ReportAnswer>()
                .eq(ReportAnswer.ITEM_ID, itemId)
                .eq(ReportAnswer.OFFICE_TYPE_ID, officeType)
                .eq(ReportAnswer.OFFICE_ID, officeId);
        int answerQuantity = reportAnswerService.selectCount(reportAnswerWrapper);
        res.setAnswerQuantity(answerQuantity);
        // 全院排名
        if (CollectionUtils.isNotEmpty(res.getQuestionList())) {
            Set<Integer> officeTypeIds = settingList.stream().map(ItemTempOffice::getOfficeType).collect(Collectors.toSet());
            Set<Integer> officeIds = settingList.stream().map(ItemTempOffice::getOfficeId).collect(Collectors.toSet());
            List<ReportAnswerQuantity> quantityAllList = reportAnswerQuantityService.getItemAllOfficeSatisfyList(itemId, officeTypeIds, officeIds);
            // 排序
            quantityAllList.sort(Comparator.comparing(ReportAnswerQuantity::getSatisfyValue).reversed());
            // 排序次数过滤
            int rank = 0;
            double rankValue = 0;
            int tempRank = 0;
            for (ReportAnswerQuantity t : quantityAllList) {
                if (rank == 0) {
                    rank++;
                    rankValue = t.getSatisfyValue();
                    t.setQuantity(rank);
                } else if (rankValue != t.getSatisfyValue()) {
                    rank++;
                    rankValue = t.getSatisfyValue();
                    rank += tempRank;
                    tempRank = 0;
                } else {
                    tempRank++;
                }
                t.setQuantity(rank);
            }
            res.getQuestionList().forEach(q -> {
                Optional<ReportAnswerQuantity> item = quantityAllList.stream().filter(v -> v.getQuestionId().equals(q.getQuestionId())).findFirst();
                if (item.isPresent()) {
                    q.setHospitalSatisfyValue(item.get().getSatisfyValue());
                    q.setQuestionLevel(item.get().getQuantity());
                }
            });
        }


        // 排序
        settingList.sort(Comparator.comparing(ItemTempOffice::getSatisfyValue).reversed());
        // 排序次数过滤
        int rank = 0;
        double rankValue = 0;
        int tempRank = 0;
        for (ItemTempOffice t : settingList) {
            if (rank == 0) {
                rank++;
                rankValue = t.getSatisfyValue();
                t.setRanking(rank);
            } else if (rankValue != t.getSatisfyValue()) {
                rank++;
                rankValue = t.getSatisfyValue();
                rank += tempRank;
                tempRank = 0;
            } else {
                tempRank++;
            }
            t.setRanking(rank);
        }
        // 设置当前科室的排名
        Optional<ItemTempOffice> rankingRow = settingList.stream().filter(v -> v.getOfficeType().equals(officeType) && v.getOfficeId().equals(officeId)).findFirst();
        if (rankingRow.isPresent()) {
            res.setLevelValue(rankingRow.get().getRanking());
        }
        return res;
    }

    @Override
    public void setItemOfficeRanking(Integer itemId) {
        new Thread(()->{
            execMakeItemOfficeScore(itemId);
        }).start();
    }

    @Override
    public String outputSingleOfficeSatisfy(OutputParam param) {
        if (CollectionUtils.isEmpty(param.getOfficeIds())) {
            Wrapper<ReportAnswer> reportAnswerWrapper = new EntityWrapper<ReportAnswer>()
                    .setSqlSelect(ReportAnswer.OFFICE_ID.concat(" as officeId"))
                    .eq(ReportAnswer.ITEM_ID, param.getItemId())
                    .eq(ReportAnswer.OFFICE_TYPE_ID, param.getOfficeType())
                    .groupBy(ReportAnswer.OFFICE_ID);
            List<ReportAnswer> reportAnswers = reportAnswerService.selectList(reportAnswerWrapper);
            List<Integer> officeIds = reportAnswers.stream().map(ReportAnswer::getOfficeId).collect(Collectors.toList());
            param.setOfficeIds(officeIds);
        }
        Wrapper<ItemOffice> itemOfficeWrapper = new EntityWrapper<ItemOffice>()
                .setSqlSelect(ItemOffice.GROUP_NAME.concat(" as groupName"), ItemOffice.TITLE, ItemOffice.OFFICE_ID.concat(" as officeId"))
                .eq(ItemOffice.ITEM_ID, param.getItemId())
                .eq(ItemOffice.OFFICE_TYPE_ID, param.getOfficeType())
                .in(ReportAnswer.OFFICE_ID, param.getOfficeIds());
        List<ItemOffice> itemOfficeList = itemOfficeService.selectList(itemOfficeWrapper);

        // 开始导出
        String path = "temp";
        initPath(path);
        String fileName = System.currentTimeMillis() +".xls";

        if (itemOfficeList.size() == 0) {
            throw new ReportException("无数据，无法导出！");
        }

        new Thread(()-> {
            try {
                File file = new File(path + "/" + fileName);
                WritableWorkbook wb = Workbook.createWorkbook(file);
                // 表头背景
                WritableCellFormat format = new WritableCellFormat();
                format.setBackground(Colour.RED);
                // 改变默认颜色
                Color color = Color.decode("#EEA9B8");
                wb.setColourRGB(Colour.RED, color.getRed(), color.getGreen(), color.getBlue());

                int groupId = 0;
                for (ItemOffice office : itemOfficeList) {
                    SingleOfficeSatisfyData itemSingleOfficeSatisfy = getItemSingleOfficeSatisfy(param.getItemId(), param.getOfficeType(), office.getOfficeId());
                    WritableSheet sheet = wb.createSheet(itemSingleOfficeSatisfy.getOfficeName(), groupId);
                    groupId++;

                    int linePos = 0;

                    // 第一行
                    sheet.addCell(new Label(0, linePos, "科室名称", format));
                    sheet.mergeCells(1, linePos, 5, linePos);
                    sheet.addCell(new Label(1, 0, itemSingleOfficeSatisfy.getOfficeName()));
                    sheet.mergeCells(6, linePos, 7, linePos);
                    sheet.addCell(new Label(6, linePos, "样本量", format));
                    sheet.mergeCells(8, linePos, 9, linePos);
                    sheet.addCell(new Label(8, linePos, itemSingleOfficeSatisfy.getAnswerQuantity()+""));
                    // 第二行
                    linePos++;
                    sheet.addCell(new Label(0, linePos, "患者满意度", format));
                    sheet.mergeCells(1, linePos, 5, linePos);
                    sheet.addCell(new Label(1, linePos, String.format("%.2f", itemSingleOfficeSatisfy.getOfficeSatisfyValue())+""));
                    sheet.mergeCells(6, linePos, 7, linePos);
                    sheet.addCell(new Label(6, linePos, "全院排名", format));
                    sheet.mergeCells(8, linePos, 9, linePos);
                    sheet.addCell(new Label(8, linePos, itemSingleOfficeSatisfy.getLevelValue()+""));
                    // 第三行
                    linePos++;
                    sheet.addCell(new Label(0, linePos, "问卷题目", format));
                    sheet.addCell(new Label(1, linePos, "非常满意", format));
                    sheet.addCell(new Label(2, linePos, "满意", format));
                    sheet.addCell(new Label(3, linePos, "一般", format));
                    sheet.addCell(new Label(4, linePos, "不满意", format));
                    sheet.addCell(new Label(5, linePos, "非常不满意", format));
                    sheet.addCell(new Label(6, linePos, "满意人数占比", format));
                    sheet.addCell(new Label(7, linePos, "满意度", format));
                    sheet.addCell(new Label(8, linePos, "院均满意度", format));
                    sheet.addCell(new Label(9, linePos, "全院排名", format));

                    // 写入数据
                    for (SingleOfficeData row : itemSingleOfficeSatisfy.getQuestionList()) {
                        linePos++;
                        sheet.addCell(new Label(0, linePos, row.getQuestionName()+"", format));
                        sheet.addCell(new Label(1, linePos, String.format("%.2f", row.getValue1())+"%"));
                        sheet.addCell(new Label(2, linePos, String.format("%.2f", row.getValue2())+"%"));
                        sheet.addCell(new Label(3, linePos, String.format("%.2f", row.getValue3())+"%"));
                        sheet.addCell(new Label(4, linePos, String.format("%.2f", row.getValue4())+"%"));
                        sheet.addCell(new Label(5, linePos, String.format("%.2f", row.getValue5())+"%"));
                        sheet.addCell(new Label(6, linePos, String.format("%.2f", row.getCountValue())+"%"));
                        sheet.addCell(new Label(7, linePos, String.format("%.2f", row.getQuestionSatisfyValue())+""));
                        sheet.addCell(new Label(8, linePos, String.format("%.2f", row.getHospitalSatisfyValue())+""));
                        sheet.addCell(new Label(9, linePos, row.getQuestionLevel()+""));
                    }

                    // 列宽度
                    sheet.setColumnView(0, 30);
                    for (int i = 1; i < 9; i++) {
                        sheet.setColumnView(i, 16);
                    }
                }
                wb.write();
                wb.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (WriteException e) {
                e.printStackTrace();
            }
        }).start();

        return path + "/" + fileName;
    }

    public CurrentOfficeBean getCurrentOfficeInfo(List<ReportAnswerOption> optionList, Integer officeId) {
        CurrentOfficeBean bean = new CurrentOfficeBean();

//        // 当前科室抽样量
//        long answerQuantity = optionList.stream().filter(v->v.getOfficeId().equals(officeId)).map(ReportAnswerOption::getAnswerId).distinct().count();
//        bean.setAnswerQuantity((int) answerQuantity);

        // 算排名
        List<TitleValueDataVO> rankingList = new ArrayList<>();
        // 根据科室ID划分
        Map<Integer, List<ReportAnswerOption>> officeGroup = optionList.stream().collect(Collectors.groupingBy(ReportAnswerOption::getOfficeId));
        for (List<ReportAnswerOption> office : officeGroup.values()) {
            ReportAnswerOption officeRow = office.get(0);
            int officeNumber = 0;
            double officeScore = 0;
            // 计算每道题的得分
            Map<Integer, List<ReportAnswerOption>> questionGroup = office.stream().collect(Collectors.groupingBy(ReportAnswerOption::getQuestionId));
            for (List<ReportAnswerOption> option : questionGroup.values()) {
                int number = 0;
                int score = 0;
                // 累计
                for (ReportAnswerOption row : option) {
                    number += row.getQuantity();
                    score += row.getQuantity() * row.getScore();
                }
                // 满意度
                if (NumberUtil.nonZero(number)) {
                    double val = (double)score / (double)number;
                    officeScore += new BigDecimal(val).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                    officeNumber++;
                }
            }
            // 科室得分
            double score = new BigDecimal(officeScore / (double) officeNumber).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            TitleValueDataVO t = new TitleValueDataVO();
            t.setId(officeRow.getOfficeId());
            t.setValue(score);
            rankingList.add(t);
        }

        // 当前科室满意度
        rankingList.sort(Comparator.comparing(TitleValueDataVO::getValue).reversed());
        // 排序次数过滤
        int rank = 0;
        double rankValue = 0;
        int tempRank = 0;
        for (TitleValueDataVO t : rankingList) {
            if (rank == 0) {
                rank++;
                rankValue = t.getValue();
                t.setRankValue(rank);
            } else if (rankValue != t.getValue()) {
                rank++;
                rankValue = t.getValue();
                rank += tempRank;
                tempRank = 0;
            } else {
                tempRank++;
            }
            t.setRankValue(rank);
        }
        Optional<TitleValueDataVO> voTemp = rankingList.stream().filter(v -> v.getId().equals(officeId)).findFirst();
        if (voTemp.isPresent()) {
            TitleValueDataVO vo = voTemp.get();
            bean.setLevel(vo.getRankValue());
            bean.setSatisfyValue(vo.getValue());
        }
        return bean;
    }

    private List<SingleOfficeData> getCurrentQuestionScore(List<ReportAnswerOption> optionList, Integer officeId) {
        List<SingleOfficeData> result = new ArrayList<>();
        List<ReportAnswerOption> currentOfficeQuestionList = optionList.stream().filter(v -> v.getOfficeId().equals(officeId)).collect(Collectors.toList());
        // 按 题目 划分
        Map<Integer, List<ReportAnswerOption>> questionGroup = currentOfficeQuestionList.stream().collect(Collectors.groupingBy(ReportAnswerOption::getQuestionId));
        for (Integer questionId : questionGroup.keySet()) {
            List<ReportAnswerOption> options = questionGroup.get(questionId);
            // 结果
            SingleOfficeData data = new SingleOfficeData();
            data.setQuestionId(questionId);
            // 计算题目满意度
            int number = 0;
            int score = 0;
            // 累计
            for (ReportAnswerOption row : options) {
                number += row.getQuantity();
                score += row.getQuantity() * row.getScore();
            }
            // 记录题目答题数量
            data.setValue1(0.0D);
            data.setValue2(0.0D);
            data.setValue3(0.0D);
            data.setValue4(0.0D);
            data.setValue5(0.0D);
            options.sort(Comparator.comparing(ReportAnswerOption::getScore).reversed());
            for (int i = 1; i <= options.size(); i++) {
                ReportAnswerOption reportAnswerOption = options.get(i - 1);
                switch (reportAnswerOption.getScore()) {
                    case 100:
                        double val1 = (double) reportAnswerOption.getQuantity() / (double) number;
                        data.setValue1(new BigDecimal(val1 * 100).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                        break;
                    case 80:
                        double val2 = (double) reportAnswerOption.getQuantity() / (double) number;
                        data.setValue2(new BigDecimal(val2 * 100).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                        break;
                    case 60:
                        double val3 = (double) reportAnswerOption.getQuantity() / (double) number;
                        data.setValue3(new BigDecimal(val3 * 100).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                        break;
                    case 40:
                        double val4 = (double) reportAnswerOption.getQuantity() / (double) number;
                        data.setValue4(new BigDecimal(val4 * 100).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                        break;
                    case 20:
                        double val5 = (double) reportAnswerOption.getQuantity() / (double) number;
                        data.setValue5(new BigDecimal(val5 * 100).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                        break;
                    default:
                        break;
                }
            }
            data.setCountValue(data.getValue1() + data.getValue2());
            // 满意度
            if (NumberUtil.nonZero(number)) {
                double val = new BigDecimal((double)score / (double)number).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                data.setQuestionSatisfyValue(val);
            }
            result.add(data);
        }
        // 设置题目标题
        List<Integer> questionIds = result.stream().map(SingleOfficeData::getQuestionId).collect(Collectors.toList());
        Wrapper<Question> questionWrapper = new EntityWrapper<Question>()
                .setSqlSelect(Question.ID, Question.TITLE)
                .in(Question.ID, questionIds);
        List<Question> tempQuestionList = questionService.selectList(questionWrapper);
        Map<Integer, String> questionMap = tempQuestionList.stream().collect(Collectors.toMap(Question::getId, Question::getTitle));
        result.forEach(row -> {
            String title = questionMap.get(row.getQuestionId());
            if (Objects.nonNull(title)) {
                row.setQuestionName(title);
            }
        });
        return result;
    }

    private List<TitleValueDataVO> getItemQuestionScore(List<ReportAnswerOption> optionList) {
        List<TitleValueDataVO> result = new ArrayList<>();
        // 按三级指标划分
        Map<Integer, List<ReportAnswerOption>> questionGroup = optionList.stream().collect(Collectors.groupingBy(ReportAnswerOption::getQuestionId));
        for (Integer questionId : questionGroup.keySet()) {
            List<ReportAnswerOption> options = questionGroup.get(questionId);
            int number = 0;
            int score = 0;
            // 累计
            for (ReportAnswerOption row : options) {
                number += row.getQuantity();
                score += row.getQuantity() * row.getScore();
            }
            // 满意度
            if (NumberUtil.nonZero(number)) {
                double val = new BigDecimal((double)score / (double)number).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                TitleValueDataVO vo = new TitleValueDataVO();
                vo.setId(questionId);
                vo.setValue(val);
                result.add(vo);
            }
        }
        // 按分数排序
        result.sort(Comparator.comparing(TitleValueDataVO::getValue).reversed());
        // 排序次数过滤
        int rank = 0;
        double rankValue = 0;
        int tempRank = 0;
        for (TitleValueDataVO t : result) {
            if (rank == 0) {
                rank++;
                rankValue = t.getValue();
                t.setRankValue(rank);
            } else if (rankValue != t.getValue()) {
                rank++;
                rankValue = t.getValue();
                rank += tempRank;
                tempRank = 0;
            } else {
                tempRank++;
            }
            t.setRankValue(rank);
        }
        return result;
    }

    private List<ReportItemScore> getItemOfficeScore(Integer itemId, List<ReportAnswerOption> optionList) {
        List<ReportItemScore> result = new ArrayList<>();

        // 根据科室类型分组
        Map<Integer, List<ReportAnswerOption>> officeTypeGroup = optionList.stream().collect(Collectors.groupingBy(ReportAnswerOption::getOfficeTypeId));
        for (List<ReportAnswerOption> type : officeTypeGroup.values()) {
            ReportAnswerOption typeRow = type.get(0);
            // 根据科室ID划分
            Map<Integer, List<ReportAnswerOption>> officeGroup = type.stream().collect(Collectors.groupingBy(ReportAnswerOption::getOfficeId));
            for (List<ReportAnswerOption> office : officeGroup.values()) {
                ReportAnswerOption officeRow = office.get(0);
                int officeNumber = 0;
                double officeScore = 0;
                // 计算每道题的得分
                Map<Integer, List<ReportAnswerOption>> questionGroup = office.stream().collect(Collectors.groupingBy(ReportAnswerOption::getQuestionId));
                for (List<ReportAnswerOption> option : questionGroup.values()) {
                    int number = 0;
                    int score = 0;
                    // 累计
                    for (ReportAnswerOption row : option) {
                        number += row.getQuantity();
                        score += row.getQuantity() * row.getScore();
                    }
                    // 满意度
                    if (NumberUtil.nonZero(number)) {
                        double val = (double)score / (double)number;
                        officeScore += new BigDecimal(val).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                        officeNumber++;
                    }
                }
                // 科室得分
                double score = new BigDecimal(officeScore / (double) officeNumber).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                // 保存数据
                ReportItemScore data = new ReportItemScore();
                data.setItemId(itemId);
                data.setOfficeTypeId(typeRow.getOfficeTypeId());
                data.setIdType(1);
                data.setIdValue(officeRow.getOfficeId());
                data.setScore(Float.parseFloat(score + ""));
                result.add(data);
//                reportItemScoreService.insert(data);
            }
        }

        for (List<ReportAnswerOption> type : officeTypeGroup.values()) {
            ReportAnswerOption typeRow = type.get(0);
            // 按三级指标划分
            Map<Integer, List<ReportAnswerOption>> targetThreeGroup = type.stream().collect(Collectors.groupingBy(ReportAnswerOption::getTargetThree));
            for (List<ReportAnswerOption> targetThree : targetThreeGroup.values()) {
                ReportAnswerOption targetThreeRow = targetThree.get(0);
                int targetNumber = 0;
                double targetScore = 0;
                // 计算每道题的得分
                Map<Integer, List<ReportAnswerOption>> questionGroup = targetThree.stream().collect(Collectors.groupingBy(ReportAnswerOption::getQuestionId));
                for (List<ReportAnswerOption> option : questionGroup.values()) {
                    int number = 0;
                    int score = 0;
                    // 累计
                    for (ReportAnswerOption row : option) {
                        number += row.getQuantity();
                        score += row.getQuantity() * row.getScore();
                    }
                    option.sort(Comparator.comparing(ReportAnswerOption::getScore).reversed());

                    // 满意度
                    if (NumberUtil.nonZero(number)) {
                        double val = (double)score / (double)number;
                        targetScore += new BigDecimal(val).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                        targetNumber++;
                    }
                }
                // 三级指标得分
                double score = new BigDecimal(targetScore / (double) targetNumber).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                // 保存数据
                ReportItemScore data = new ReportItemScore();
                data.setItemId(itemId);
                data.setOfficeTypeId(typeRow.getOfficeTypeId());
                data.setIdType(2);
                data.setIdValue(targetThreeRow.getTargetThree());
                data.setScore(Float.parseFloat(score + ""));
                result.add(data);
//                reportItemScoreService.insert(data);
            }
        }
        return result;
    }

    private void execMakeItemOfficeScore(Integer itemId) {
        Wrapper<ReportAnswerOption> wrapper = new EntityWrapper<ReportAnswerOption>()
                .setSqlSelect(
                        ReportAnswerOption.OFFICE_TYPE_ID.concat(" as officeTypeId"),
                        ReportAnswerOption.OFFICE_ID.concat(" as officeId"),
                        ReportAnswerOption.TARGET_THREE.concat(" as targetThree"),
                        ReportAnswerOption.QUESTION_ID.concat(" as questionId"),
                        ReportAnswerOption.OPTION_ID.concat(" as optionId"),
                        ReportAnswerOption.SCORE,
                        "COUNT(1) as quantity"
                )
                .eq(ReportAnswerOption.ITEM_ID, itemId)
                .in(ReportAnswerOption.OPTION_TYPE, Arrays.asList(1, 4))
                .ne(ReportAnswerOption.SCORE, 0)
                .groupBy(ReportAnswerOption.OFFICE_TYPE_ID)
                .groupBy(ReportAnswerOption.OFFICE_ID)
                .groupBy(ReportAnswerOption.QUESTION_ID)
                .groupBy(ReportAnswerOption.OPTION_ID);
        List<ReportAnswerOption> optionList = reportAnswerOptionService.selectList(wrapper);

        // 清理历史数据
        Wrapper<ReportItemScore> reportItemScoreWrapper = new EntityWrapper<ReportItemScore>()
                .eq(ReportItemScore.ITEM_ID, itemId);
        reportItemScoreService.delete(reportItemScoreWrapper);

        // 根据科室类型分组
        Map<Integer, List<ReportAnswerOption>> officeTypeGroup = optionList.stream().collect(Collectors.groupingBy(ReportAnswerOption::getOfficeTypeId));
        for (List<ReportAnswerOption> type : officeTypeGroup.values()) {
            ReportAnswerOption typeRow = type.get(0);
            // 根据科室ID划分
            Map<Integer, List<ReportAnswerOption>> officeGroup = type.stream().collect(Collectors.groupingBy(ReportAnswerOption::getOfficeId));
            for (List<ReportAnswerOption> office : officeGroup.values()) {
                ReportAnswerOption officeRow = office.get(0);
                int officeNumber = 0;
                double officeScore = 0;
                // 计算每道题的得分
                Map<Integer, List<ReportAnswerOption>> questionGroup = office.stream().collect(Collectors.groupingBy(ReportAnswerOption::getQuestionId));
                for (List<ReportAnswerOption> option : questionGroup.values()) {
                    int number = 0;
                    int score = 0;
                    // 累计
                    for (ReportAnswerOption row : option) {
                        number += row.getQuantity();
                        score += row.getQuantity() * row.getScore();
                    }
                    // 满意度
                    if (NumberUtil.nonZero(number)) {
                        double val = (double)score / (double)number;
                        officeScore += new BigDecimal(val).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                        officeNumber++;
                    }
                }
                // 科室得分
                double score = new BigDecimal(officeScore / (double) officeNumber).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                // 保存数据
                ReportItemScore data = new ReportItemScore();
                data.setItemId(itemId);
                data.setOfficeTypeId(typeRow.getOfficeTypeId());
                data.setIdType(1);
                data.setIdValue(officeRow.getOfficeId());
                data.setScore(Float.parseFloat(score + ""));
                reportItemScoreService.insert(data);
            }
        }

        for (List<ReportAnswerOption> type : officeTypeGroup.values()) {
            ReportAnswerOption typeRow = type.get(0);
            // 按三级指标划分
            Map<Integer, List<ReportAnswerOption>> targetThreeGroup = type.stream().collect(Collectors.groupingBy(ReportAnswerOption::getTargetThree));
            for (List<ReportAnswerOption> targetThree : targetThreeGroup.values()) {
                ReportAnswerOption targetThreeRow = targetThree.get(0);
                int targetNumber = 0;
                double targetScore = 0;
                // 计算每道题的得分
                Map<Integer, List<ReportAnswerOption>> questionGroup = targetThree.stream().collect(Collectors.groupingBy(ReportAnswerOption::getQuestionId));
                for (List<ReportAnswerOption> option : questionGroup.values()) {
                    int number = 0;
                    int score = 0;
                    // 累计
                    for (ReportAnswerOption row : option) {
                        number += row.getQuantity();
                        score += row.getQuantity() * row.getScore();
                    }
                    // 满意度
                    if (NumberUtil.nonZero(number)) {
                        double val = (double)score / (double)number;
                        targetScore += new BigDecimal(val).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                        targetNumber++;
                    }
                }
                // 三级指标得分
                double score = new BigDecimal(targetScore / (double) targetNumber).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                // 保存数据
                ReportItemScore data = new ReportItemScore();
                data.setItemId(itemId);
                data.setOfficeTypeId(typeRow.getOfficeTypeId());
                data.setIdType(2);
                data.setIdValue(targetThreeRow.getTargetThree());
                data.setScore(Float.parseFloat(score + ""));
                reportItemScoreService.insert(data);
            }
        }
    }

    private void initPath(String path) {
        File file1 = new File(path);
        if ( ! file1.exists()) {
            file1.mkdirs();
        }
    }

    private String getItemOfficeNameByOfficeId(Integer itemId, Integer officeType, Integer officeId) {
        Wrapper<ItemOffice> wrapper = new EntityWrapper<ItemOffice>()
                .setSqlSelect(
                        ItemOffice.TITLE,
                        ItemOffice.GROUP_NAME.concat(" as groupName")
                )
                .eq(ItemOffice.ITEM_ID, itemId)
                .eq(ItemOffice.OFFICE_TYPE_ID, officeType)
                .eq(ItemOffice.OFFICE_ID, officeId);
        ItemOffice itemOffice = itemOfficeService.selectOne(wrapper);
        if (Objects.nonNull(itemOffice)) {
            return itemOffice.getTitle().equals("") ? itemOffice.getGroupName() : itemOffice.getTitle();
        }
        return StringUtils.EMPTY;
    }

    private List<SingleOfficeData> getSatisfyQuestionList(Integer itemId, Integer officeType, Integer officeId, List<Integer> targetIds) {
        List<SingleOfficeData> result = new ArrayList<>();
        List<ReportAnswerQuantity> questionList = reportAnswerQuantityService.
                getItemOfficeSatisfyQuestionList(itemId, officeType, officeId, targetIds);
        questionList.forEach(q -> {
            SingleOfficeData data = new SingleOfficeData();
            data.setQuestionId(q.getQuestionId());
            data.setQuestionName(q.getQuestionName());
            // 默认值
            data.setValue1(0d);
            data.setValue2(0d);
            data.setValue3(0d);
            data.setValue4(0d);
            data.setValue5(0d);
            int qty = q.getValue1() + q.getValue2() + q.getValue3() + q.getValue4() + q.getValue5();
            if (qty > 0) {
                data.setValue1((double)q.getValue1() / (double) qty * 100);
                data.setValue2((double)q.getValue2() / (double) qty * 100);
                data.setValue3((double)q.getValue3() / (double) qty * 100);
                data.setValue4((double)q.getValue4() / (double) qty * 100);
                data.setValue5((double)q.getValue5() / (double) qty * 100);
            }
            data.setCountValue(data.getValue1() + data.getValue2());
            data.setQuestionSatisfyValue(q.getSatisfyValue());
            data.setHospitalSatisfyValue(0d);
            data.setQuestionLevel(0);
            result.add(data);
        });
        return result;
    }
}

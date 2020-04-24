package com.sunchs.lyt.report.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.sunchs.lyt.db.business.entity.*;
import com.sunchs.lyt.db.business.service.impl.*;
import com.sunchs.lyt.framework.bean.IdTitleData;
import com.sunchs.lyt.framework.bean.TitleValueChildrenData;
import com.sunchs.lyt.framework.bean.TitleValueData;
import com.sunchs.lyt.framework.util.FormatUtil;
import com.sunchs.lyt.report.bean.*;
import com.sunchs.lyt.report.exception.ReportException;
import com.sunchs.lyt.report.service.IReportOutputService;
import jxl.Workbook;
import jxl.format.Colour;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.Label;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class ReportOutputService implements IReportOutputService {

    @Autowired
    private ReportAnswerServiceImpl reportAnswerService;

    @Autowired
    private ReportAnswerOptionServiceImpl reportAnswerOptionService;

    @Autowired
    private HospitalOfficeServiceImpl hospitalOfficeService;

    @Autowired
    private HospitalServiceImpl hospitalService;

    @Autowired
    private ReportTagService reportTagService;

    @Autowired
    private QuestionTagServiceImpl questionTagService;

    @Autowired
    private QuestionnaireServiceImpl questionnaireService;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private ReportRelatedService reportRelatedService;

    @Autowired
    private ItemOfficeServiceImpl itemOfficeService;

    @Autowired
    private ReportTargetService reportTargetService;

    @Autowired
    private ReportSettingService reportSettingService;

    @Autowired
    private ReportCustomOfficeService reportCustomOfficeService;

    @Override
    public String getItemOfficeAnswer(OutputParam param) {
        String path = "temp";
        initPath(path);
        String fileName = System.currentTimeMillis() +".xls";

        Wrapper<ReportAnswer> reportAnswerWrapper = new EntityWrapper<ReportAnswer>();
        if (CollectionUtils.isNotEmpty(param.getOfficeIds())) {
            reportAnswerWrapper.eq(ReportAnswer.ITEM_ID, param.getItemId())
                    .in(ReportAnswer.OFFICE_ID, param.getOfficeIds());
        } else if (param.getOfficeId() != 0){
            reportAnswerWrapper.eq(ReportAnswer.ITEM_ID, param.getItemId())
                    .eq(ReportAnswer.OFFICE_ID, param.getOfficeId());
        } else {
            reportAnswerWrapper.eq(ReportAnswer.ITEM_ID, param.getItemId());
        }
        // 按照时间段导出
        if (Objects.nonNull(param.getStartTime()) && param.getStartTime().length() > 0) {
            reportAnswerWrapper.ge(ReportAnswer.STARTTIME, param.getStartTime())
                    .le(ReportAnswer.STARTTIME, param.getEndTime());
        }
        int qty = reportAnswerService.selectCount(reportAnswerWrapper);
        if (qty == 0) {
            throw new ReportException("无数据，无法导出！");
        }

        new Thread(()-> {
            List<ReportAnswer> reportAnswerList = reportAnswerService.selectList(reportAnswerWrapper);
            if (reportAnswerList.size() == 0) {
                throw new ReportException("无数据，无法导出！");
            }

            List<Integer> reportAnswerIds = reportAnswerList.stream().map(ReportAnswer::getId).collect(Collectors.toList());
            Map<Integer, List<ReportAnswer>> answerGroupList = reportAnswerList.stream().collect(Collectors.groupingBy(ReportAnswer::getQuestionnaireId));

            // 获取题目
            Wrapper<ReportAnswerOption> reportAnswerOptionWrapper = new EntityWrapper<ReportAnswerOption>()
                    .in(ReportAnswerOption.ANSWER_ID, reportAnswerIds)
                    .groupBy(ReportAnswerOption.QUESTIONNAIRE_ID)
                    .groupBy(ReportAnswerOption.QUESTION_ID);
            List<ReportAnswerOption> reportAnswerOptionGroupList = reportAnswerOptionService.selectList(reportAnswerOptionWrapper);
            // 按问卷分组
            Map<Integer, List<ReportAnswerOption>> reportAnswerOptionGroupMap = reportAnswerOptionGroupList.stream().collect(Collectors.groupingBy(ReportAnswerOption::getQuestionnaireId));

            Wrapper<ReportAnswerOption> wrapper = new EntityWrapper<ReportAnswerOption>()
                    .in(ReportAnswerOption.ANSWER_ID, reportAnswerIds);
            List<ReportAnswerOption> reportAnswerOptionList = reportAnswerOptionService.selectList(wrapper);
//        Map<Integer, List<ReportAnswerOption>> questionMap = reportAnswerOptionList.stream().collect(Collectors.groupingBy(ReportAnswerOption::getQuestionId));

            // 医院和科室名称
            Integer hospitalId = reportAnswerList.get(0).getHospitalId();
            String hospitalName = getHospitalNameById(hospitalId);
            List<Integer> officeIds = reportAnswerList.stream().map(ReportAnswer::getOfficeId).distinct().collect(Collectors.toList());
            Wrapper<ItemOffice> itemOfficeWrapper = new EntityWrapper<ItemOffice>()
                    .eq(ItemOffice.HOSPITAL_ID, hospitalId)
                    .in(ItemOffice.OFFICE_ID, officeIds)
                    .groupBy(ItemOffice.OFFICE_ID);
            List<ItemOffice> itemOfficeTempList = itemOfficeService.selectList(itemOfficeWrapper);
            Map<Integer, String> officeNameMap = itemOfficeTempList.stream().collect(Collectors.toMap(ItemOffice::getOfficeId, ItemOffice::getTitle));
            officeNameMap.put(0, "员工");

            // 问卷集合
            Wrapper<Questionnaire> questionnaireWrapper = new EntityWrapper<Questionnaire>()
                    .in(Questionnaire.ID, answerGroupList.keySet());
            List<Questionnaire> questionnaireTempList = questionnaireService.selectList(questionnaireWrapper);
            Map<Integer, String> questionnaireNameMap = questionnaireTempList.stream().collect(Collectors.toMap(Questionnaire::getId, Questionnaire::getTitle));

            // 用户集合
            List<Integer> userIds = reportAnswerList.stream().map(ReportAnswer::getUserId).distinct().collect(Collectors.toList());
            Wrapper<User> userWrapper = new EntityWrapper<User>()
                    .in(User.ID, userIds);
            List<User> userTempList = userService.selectList(userWrapper);
            Map<Integer, String> userNameMap = userTempList.stream().collect(Collectors.toMap(User::getId, User::getName));

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
                for (Integer questionnaireId : answerGroupList.keySet()) {
                    List<ReportAnswer> answerList = answerGroupList.get(questionnaireId);
                    WritableSheet sheet = wb.createSheet(questionnaireNameMap.get(questionnaireId), groupId);
                    groupId++;

                    int columnPos = 0;
                    int linePos = 0;
                    sheet.addCell(new Label(columnPos++, linePos, "调查员账号", format));
                    sheet.addCell(new Label(columnPos++, linePos, "病人ID", format));
                    sheet.addCell(new Label(columnPos++, linePos, "调查医院", format));
                    sheet.addCell(new Label(columnPos++, linePos, "调查科室", format));
                    sheet.addCell(new Label(columnPos++, linePos, "调查问卷", format));
                    sheet.addCell(new Label(columnPos++, linePos, "调查开始", format));
                    sheet.addCell(new Label(columnPos++, linePos, "调查结束", format));
                    for (ReportAnswerOption answerOption : reportAnswerOptionGroupMap.get(questionnaireId)) {
                        sheet.addCell(new Label(columnPos++, linePos, answerOption.getQuestionName(), format));
                    }
                    // 列宽度
                    for (int i = 0; i < columnPos; i++) {
                        sheet.setColumnView(i, 18);
                    }
                    // 写入数据
                    for (ReportAnswer answer : answerList) {
                        columnPos = 0;
                        linePos++;
                        sheet.addCell(new Label(columnPos++, linePos, userNameMap.get(answer.getCreateId())));
                        sheet.addCell(new Label(columnPos++, linePos, answer.getPatientNumber()));
                        sheet.addCell(new Label(columnPos++, linePos, hospitalName));
                        sheet.addCell(new Label(columnPos++, linePos, officeNameMap.get(answer.getOfficeId())));
                        sheet.addCell(new Label(columnPos++, linePos, questionnaireNameMap.get(questionnaireId)));
                        sheet.addCell(new Label(columnPos++, linePos, FormatUtil.dateTime(answer.getStartTime())));
                        sheet.addCell(new Label(columnPos++, linePos, FormatUtil.dateTime(answer.getEndTime())));

                        for (ReportAnswerOption answerOption : reportAnswerOptionGroupMap.get(questionnaireId)) {
                            List<ReportAnswerOption> optionList = reportAnswerOptionList.stream().filter(v ->
                                    v.getAnswerId().equals(answer.getId()) && v.getQuestionId().equals(answerOption.getQuestionId())
                            ).collect(Collectors.toList());
                            if (Objects.nonNull(optionList) && optionList.size() > 0) {
                                String value = "";
                                for (ReportAnswerOption option : optionList) {
                                    value += value.equals("") ? option.getOptionName() : ","+option.getOptionName();
                                }
                                sheet.addCell(new Label(columnPos, linePos, value));
                            } else {
                                sheet.addCell(new Label(columnPos, linePos, " "));
                            }
                            columnPos++;
                        }
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

    @Override
    public String getItemTargetTag(OutputParam param) {
        List<TotalSexData> list = reportTagService.getItemQuantityByTag(param.getItemId(), param.getTagId(), param.getTargetOne());

        Wrapper<QuestionTag> wrapper = new EntityWrapper<QuestionTag>()
                .eq(QuestionTag.ID, param.getTagId());
        QuestionTag tag = questionTagService.selectOne(wrapper);
        if (Objects.isNull(tag)) {
            throw new ReportException("无对应的标签数据");
        }

        String path = "temp";
        initPath(path);
        String fileName = System.currentTimeMillis() +".xls";

        try {
            File file = new File(path + "/" + fileName);
            WritableWorkbook wb = jxl.Workbook.createWorkbook(file);
            // 改变默认颜色
            Color color = Color.decode("#EEA9B8");
            wb.setColourRGB(Colour.RED, color.getRed(), color.getGreen(), color.getBlue());
            WritableSheet sheet = wb.createSheet(tag.getTitle(), 0);
            // 写表头
            WritableCellFormat format = new WritableCellFormat();
            format.setBackground(Colour.RED);

            int columnPos = 1;
            sheet.addCell(new Label(0, 0, " ", format));
            for (TotalSexData data : list) {
                sheet.addCell(new Label(columnPos++, 0, data.getOptionName(), format));
            }
            sheet.addCell(new Label(0, 1, "人数", format));
            sheet.addCell(new Label(0, 2, "占比", format));
            // 列宽度
            for (int i = 0; i < columnPos; i++) {
                sheet.setColumnView(i, 18);
            }

            for (int i = 0; i < list.size(); i++) {
                sheet.addCell(new Label(i+1, 1, list.get(i).getQuantity()+""));
            }
            for (int i = 0; i < list.size(); i++) {
                sheet.addCell(new Label(i+1, 2, list.get(i).getRate()+""));
            }

            wb.write();
            wb.close();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (WriteException e) {
            e.printStackTrace();
        }
        return path + "/" + fileName;
    }

    @Override
    public String getItemRelatedData(OutputParam param) {
        ItemRelatedData list = reportRelatedService.getItemRelatedData(param.getItemId(), param.getOfficeType());

        if (CollectionUtils.isEmpty(list.getColList()) || CollectionUtils.isEmpty(list.getRowList())) {
            throw new ReportException("无数据");
        }

        String path = "temp";
        initPath(path);
        String fileName = System.currentTimeMillis() +".xls";

        try {
            File file = new File(path + "/" + fileName);
            WritableWorkbook wb = jxl.Workbook.createWorkbook(file);
            // 改变默认颜色
            Color color = Color.decode("#EEA9B8");
            wb.setColourRGB(Colour.RED, color.getRed(), color.getGreen(), color.getBlue());
            WritableSheet sheet = wb.createSheet("相关系数", 0);
            // 写表头
            WritableCellFormat format = new WritableCellFormat();
            format.setBackground(Colour.RED);

            // 列标题
            int columnPos = 1;
            for (IdTitleData data : list.getColList()) {
                sheet.addCell(new Label(columnPos++, 0, data.getTitle(), format));
            }

            // 列宽度
            for (int i = 0; i < columnPos; i++) {
                sheet.setColumnView(i, 8);
            }

            // 行标题
            int line = 1;
            for (IdTitleData row : list.getRowList()) {
                sheet.addCell(new Label(0, line++, row.getTitle(), format));
//                sheet.setRowView(line, 30);
            }

            // 值
            line = 1;
            for (IdTitleData row : list.getRowList()) {
                columnPos = 1;
                for (IdTitleData col : list.getColList()) {
                    List<ItemCompareValue> valueList = list.getValueList();
                    sheet.addCell(new Label(columnPos++, line, getRelatedValue(valueList, col.getId(), row.getId())));
                }
                line++;
            }

            wb.write();
            wb.close();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (WriteException e) {
            e.printStackTrace();
        }
        return path + "/" + fileName;
    }

    @Override
    public String getItemSatisfyReport(OutputParam param) {
        String path = "temp";
        initPath(path);
        String fileName = System.currentTimeMillis() +".xls";
        try {
            File file = new File(path + "/" + fileName);
            WritableWorkbook wb = jxl.Workbook.createWorkbook(file);
            // 改变默认颜色
            Color color = Color.decode("#EEA9B8");
            wb.setColourRGB(Colour.RED, color.getRed(), color.getGreen(), color.getBlue());
            WritableSheet sheet = wb.createSheet("满意度报表", 0);
            // 写表头
            WritableCellFormat format = new WritableCellFormat();
            format.setBackground(Colour.RED);

            int column = 0;
            int line = 0;
            // 开始写数据

            // 总体满意度
            Double itemAllSatisfy = reportTargetService.getItemAllSatisfy(param.getItemId(), param.getOfficeType());
            sheet.addCell(new Label(column++, line, "综合满意度", format));
            sheet.addCell(new Label(column++, line, itemAllSatisfy+""));
            line++;
            column = 0;

            // 总体满意度
            List<SatisfyData> twoSatisfyList = reportTargetService.getItemSatisfyByTarget(param.getItemId(), param.getOfficeType(), 1);
            Optional<SatisfyData> questionSatisfy = twoSatisfyList.stream().filter(v -> v.getName().equals("总体满意度")).findFirst();
            if (questionSatisfy.isPresent()) {
                line++;
                sheet.addCell(new Label(column++, line, "总体满意度", format));
                sheet.addCell(new Label(column++, line, questionSatisfy.get().getValue()+""));
                line++;
                column = 0;
            }
            Optional<SatisfyData> questionSatisfy2 = twoSatisfyList.stream().filter(v -> v.getName().equals("满意度")).findFirst();
            if (questionSatisfy2.isPresent()) {
                line++;
                sheet.addCell(new Label(column++, line, "满意度", format));
                sheet.addCell(new Label(column++, line, questionSatisfy2.get().getValue()+""));
                line++;
                column = 0;
            }

            Optional<SatisfyData> pushSatisfy = twoSatisfyList.stream().filter(v -> v.getName().equals("推荐度")).findFirst();
            if (pushSatisfy.isPresent()) {
                line++;
                sheet.addCell(new Label(column++, line, "推荐度", format));
                sheet.addCell(new Label(column++, line, pushSatisfy.get().getValue()+""));
                line++;
                column = 0;
            }

            // 二级指标满意度
            line++;
            List<SatisfyData> twoSatisfy = twoSatisfyList.stream().filter(v ->
                    ( ! v.getName().equals("总体满意度")) && ( ! v.getName().equals("满意度")) && ( ! v.getName().equals("推荐度"))
            ).collect(Collectors.toList());
            sheet.addCell(new Label(0, line++, "二级指标满意度", format));
            for (SatisfyData row : twoSatisfy) {
                sheet.addCell(new Label(column++, line, row.getName(), format));
            }
            line++;
            column = 0;
            for (SatisfyData row : twoSatisfy) {
                sheet.addCell(new Label(column++, line, row.getValue()+""));
            }
            line++;
            column = 0;

            // 三级指标满意度
            line++;
            sheet.addCell(new Label(0, line++, "三级级指标满意度", format));
            for (SatisfyData row : twoSatisfy) {
                List<SatisfyData> threeList = reportTargetService.getItemSatisfyByTarget(param.getItemId(), row.getId(), 2);
                for (SatisfyData three : threeList) {
                    sheet.addCell(new Label(column++, line, three.getpName(), format));
                    sheet.addCell(new Label(column++, line, three.getName(), format));
                    sheet.addCell(new Label(column++, line, three.getValue()+""));
                    line++;
                    column = 0;
                }
            }
            line++;
            column = 0;

            // 性别
            List<TotalSexData> sexList = reportTagService.getItemQuantityByTag(param.getItemId(), 2, param.getOfficeType());
            if (CollectionUtils.isNotEmpty(sexList)) {
                line++;
                sheet.addCell(new Label(0, line++, "性别", format));

                sheet.addCell(new Label(column++, line, "", format));
                for (TotalSexData sexData : sexList) {
                    sheet.addCell(new Label(column++, line, sexData.getOptionName() + "", format));
                }
                line++;
                column = 0;
                sheet.addCell(new Label(column++, line, "人数", format));
                for (TotalSexData sexData : sexList) {
                    sheet.addCell(new Label(column++, line, sexData.getQuantity() + ""));
                }
                line++;
                column = 0;
                sheet.addCell(new Label(column++, line, "占比", format));
                for (TotalSexData sexData : sexList) {
                    sheet.addCell(new Label(column++, line, sexData.getRate() + "%"));
                }
                line++;
                column = 0;
            }

            // 年龄
            List<TotalSexData> ageList = reportTagService.getItemQuantityByTag(param.getItemId(), 3, param.getOfficeType());
            if (CollectionUtils.isNotEmpty(ageList)) {
                line++;
                sheet.addCell(new Label(0, line++, "年龄", format));

                sheet.addCell(new Label(column++, line, "", format));
                for (TotalSexData ageData : ageList) {
                    sheet.addCell(new Label(column++, line, ageData.getOptionName() + "", format));
                }
                line++;
                column = 0;
                sheet.addCell(new Label(column++, line, "人数", format));
                for (TotalSexData ageData : ageList) {
                    sheet.addCell(new Label(column++, line, ageData.getQuantity() + ""));
                }
                line++;
                column = 0;
                sheet.addCell(new Label(column++, line, "占比", format));
                for (TotalSexData ageData : ageList) {
                    sheet.addCell(new Label(column++, line, ageData.getRate() + "%"));
                }
                line++;
                column = 0;
            }

            // 来院理由
            List<TotalSexData> fromList = reportTagService.getItemQuantityByTag(param.getItemId(), 4, param.getOfficeType());
            if (CollectionUtils.isNotEmpty(fromList)) {
                line++;
                sheet.addCell(new Label(0, line++, "来院理由", format));
                sheet.addCell(new Label(column++, line, "", format));
                for (TotalSexData fromData : fromList) {
                    sheet.addCell(new Label(column++, line, fromData.getOptionName() + "", format));
                }
                line++;
                column = 0;
                sheet.addCell(new Label(column++, line, "人数", format));
                for (TotalSexData fromData : fromList) {
                    sheet.addCell(new Label(column++, line, fromData.getQuantity() + ""));
                }
                line++;
                column = 0;
                sheet.addCell(new Label(column++, line, "占比", format));
                for (TotalSexData fromData : fromList) {
                    sheet.addCell(new Label(column++, line, fromData.getRate() + "%"));
                }
                line++;
                column = 0;
            }

            // 居住地
            List<TotalSexData> addressList = reportTagService.getItemQuantityByTag(param.getItemId(), 5, param.getOfficeType());
            if (CollectionUtils.isNotEmpty(addressList)) {
                line++;
                sheet.addCell(new Label(0, line++, "居住地", format));

                sheet.addCell(new Label(column++, line, "", format));
                for (TotalSexData addressData : addressList) {
                    sheet.addCell(new Label(column++, line, addressData.getOptionName()+"", format));
                }
                line++;
                column = 0;
                sheet.addCell(new Label(column++, line, "人数", format));
                for (TotalSexData addressData : addressList) {
                    sheet.addCell(new Label(column++, line, addressData.getQuantity()+""));
                }
                line++;
                column = 0;
                sheet.addCell(new Label(column++, line, "占比", format));
                for (TotalSexData addressData : addressList) {
                    sheet.addCell(new Label(column++, line, addressData.getRate()+"%"));
                }
            }

            // 列宽度
            for (int i = 0; i < 20; i++) {
                sheet.setColumnView(i, 18);
            }

            wb.write();
            wb.close();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (WriteException e) {
            e.printStackTrace();
        }
        return path + "/" + fileName;
    }

    @Override
    public String getItemCustomOfficeSatisfyReport(OutputParam param) {
        String path = "temp";
        initPath(path);
        String fileName = System.currentTimeMillis() +".xls";
        try {
            File file = new File(path + "/" + fileName);
            WritableWorkbook wb = jxl.Workbook.createWorkbook(file);
            // 改变默认颜色
            Color color = Color.decode("#EEA9B8");
            wb.setColourRGB(Colour.RED, color.getRed(), color.getGreen(), color.getBlue());
            // 写表头
            WritableCellFormat format = new WritableCellFormat();
            format.setBackground(Colour.RED);

            int column = 0;
            int line = 0;
            int sheetIndex = 0;
            // 临床科室满意度
            Map<String, List<TitleValueChildrenData>> tempOfficeData = reportSettingService.getItemTempOfficeSatisfyAndRankingList(param.getItemId(), param.getOfficeType());
            List<TitleValueChildrenData> tempList = tempOfficeData.get("list");
            if (CollectionUtils.isNotEmpty(tempList)) {
                WritableSheet sheet = wb.createSheet("临床科室满意度", sheetIndex++);
                // 宽度
                int widthLen = 0;
                for (TitleValueChildrenData d : tempList) {
                    if (d.getChildren().size() > widthLen) {
                        widthLen = d.getChildren().size();
                    }
                }
                for (int i = 0; i < widthLen + 2; i++) {
                    sheet.setColumnView(i, 18);
                }
                // 散数据
                for (TitleValueChildrenData temp : tempList) {
                    column=0;
                    List<TitleValueData> childList = temp.getChildren();
                    if (CollectionUtils.isNotEmpty(childList)) {
                        // 标题
                        sheet.addCell(new Label(column++, line, "", format));
                        sheet.addCell(new Label(column++, line, "总满意度", format));
                        for (TitleValueData ch : childList) {
                            sheet.addCell(new Label(column++, line, ch.getTitle(), format));
                        }
                        // 内容
                        line++;
                        column=0;
                        sheet.addCell(new Label(column++, line, temp.getTitle()+""));
                        sheet.addCell(new Label(column++, line, temp.getValue()+""));
                        for (TitleValueData ch : childList) {
                            sheet.addCell(new Label(column++, line, ch.getValue()+""));
                        }
                    }
                    line++;
                    line++;
                }

                // 改变默认颜色
                Color colorRank = Color.decode("#5dc250");
                wb.setColourRGB(Colour.GREEN, colorRank.getRed(), colorRank.getGreen(), colorRank.getBlue());
                // 写表头
                WritableCellFormat formatRank = new WritableCellFormat();
                formatRank.setBackground(Colour.GREEN);
                // 排名
                line++;
                column=0;
                List<TitleValueChildrenData> rankingList = tempOfficeData.get("rankingList");
                sheet.addCell(new Label(column++, line, "排名", formatRank));
                sheet.addCell(new Label(column++, line, "科室名称", formatRank));
                sheet.addCell(new Label(column++, line, "科室满意度", formatRank));
                for (TitleValueChildrenData temp : rankingList) {
                    column=0;
                    line++;
                    sheet.addCell(new Label(column++, line, temp.getId()+""));
                    sheet.addCell(new Label(column++, line, temp.getTitle()+""));
                    sheet.addCell(new Label(column++, line, temp.getValue()+""));
                }
            }


            column = 0;
            line = 0;
            // 自定义科室满意度
            CustomOfficeDataVO customOfficeList = reportCustomOfficeService.getCustomOfficeList(param.getItemId(), param.getOfficeType());
            if (CollectionUtils.isNotEmpty(customOfficeList.getList())) {
                WritableSheet sheet = wb.createSheet("自定义科室满意度", sheetIndex++);
                // 宽度
                int widthLen = 0;
                for (CustomOfficeData d : customOfficeList.getList()) {
                    if (d.getTargetList().size() > widthLen) {
                        widthLen = d.getTargetList().size();
                    }
                }
                for (int i = 0; i < widthLen + 2; i++) {
                    sheet.setColumnView(i, 18);
                }
                // 散数据
                for (CustomOfficeData custom : customOfficeList.getList()) {
                    column=0;
                    List<CustomOfficeTargetData> childList = custom.getTargetList();
                    if (CollectionUtils.isNotEmpty(childList)) {
                        // 标题
                        sheet.addCell(new Label(column++, line, "", format));
                        sheet.addCell(new Label(column++, line, "总满意度", format));
                        for (CustomOfficeTargetData ch : childList) {
                            sheet.addCell(new Label(column++, line, ch.getTargetTitle(), format));
                        }
                        // 内容
                        line++;
                        column=0;

                        double cValue = 0.00;
                        int qty = 0;
                        for (CustomOfficeTargetData ch : childList) {
                            if (ch.getSatisfyValue() > 0) {
                                cValue += ch.getSatisfyValue();
                                qty++;
                            }
                        }
                        double value = new BigDecimal(cValue / (double) qty).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                        sheet.addCell(new Label(column++, line, custom.getTitle()+""));
                        sheet.addCell(new Label(column++, line, value+""));
                        for (CustomOfficeTargetData ch : childList) {
                            sheet.addCell(new Label(column++, line, ch.getSatisfyValue()+""));
                        }
                    }
                    line++;
                    line++;
                }

                // 改变默认颜色
                Color colorRank = Color.decode("#5dc250");
                wb.setColourRGB(Colour.GREEN, colorRank.getRed(), colorRank.getGreen(), colorRank.getBlue());
                // 写表头
                WritableCellFormat formatRank = new WritableCellFormat();
                formatRank.setBackground(Colour.GREEN);
                // 排名
                line++;
                column=0;
                sheet.addCell(new Label(column++, line, "排名", formatRank));
                sheet.addCell(new Label(column++, line, "科室名称", formatRank));
                sheet.addCell(new Label(column++, line, "科室满意度", formatRank));
                for (TitleValueDataVO valueDataVO : customOfficeList.getRankingList()) {
                    column=0;
                    line++;
                    sheet.addCell(new Label(column++, line, valueDataVO.getRankValue()+""));
                    sheet.addCell(new Label(column++, line, valueDataVO.getTitle()+""));
                    sheet.addCell(new Label(column++, line, valueDataVO.getValue()+""));
                }
            }

            wb.write();
            wb.close();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (WriteException e) {
            e.printStackTrace();
        }
        return path + "/" + fileName;
    }

    /**
     * 获取相关系数值
     */
    private String getRelatedValue(List<ItemCompareValue> valueList, Integer colId, Integer rowId) {
        Optional<ItemCompareValue> value = valueList.stream().filter(v -> v.getColId().equals(colId) && v.getRowId().equals(rowId)).findFirst();
        if (value.isPresent()) {
            return value.get().getValue().doubleValue() + "";
        }
        return "";
    }

    private void initPath(String path) {
        File file1 = new File(path);
        if ( ! file1.exists()) {
            file1.mkdirs();
        }
    }

    private String getHospitalNameById(int hospitalId) {
        Wrapper<Hospital> wrapper = new EntityWrapper<Hospital>()
                .eq(Hospital.ID, hospitalId);
        Hospital row = hospitalService.selectOne(wrapper);
        if (Objects.nonNull(row)) {
            return row.getHospitalName();
        }
        return "";
    }
}

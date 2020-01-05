package com.sunchs.lyt.report.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.sunchs.lyt.db.business.entity.*;
import com.sunchs.lyt.db.business.service.impl.*;
import com.sunchs.lyt.framework.bean.IdTitleData;
import com.sunchs.lyt.framework.util.FormatUtil;
import com.sunchs.lyt.report.bean.ItemCompareValue;
import com.sunchs.lyt.report.bean.ItemRelatedData;
import com.sunchs.lyt.report.bean.OutputParam;
import com.sunchs.lyt.report.bean.TotalSexData;
import com.sunchs.lyt.report.exception.ReportException;
import com.sunchs.lyt.report.service.IReportOutputService;
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

    @Override
    public String getItemOfficeAnswer(OutputParam param) {
        Wrapper<ReportAnswer> reportAnswerWrapper = new EntityWrapper<ReportAnswer>()
                .eq(ReportAnswer.ITEM_ID, param.getItemId())
                .eq(ReportAnswer.OFFICE_ID, param.getOfficeId())
                .ge(ReportAnswer.STARTTIME, param.getStartTime())
                .le(ReportAnswer.STARTTIME, param.getEndTime());
        List<ReportAnswer> reportAnswerList = reportAnswerService.selectList(reportAnswerWrapper);
        if (reportAnswerList.size() == 0) {
            throw new ReportException("无数据，无法导出！");
        }
        List<Integer> reportAnswerIds = reportAnswerList.stream().map(ReportAnswer::getId).collect(Collectors.toList());

        // 获取题目
        Wrapper<ReportAnswerOption> reportAnswerOptionWrapper = new EntityWrapper<ReportAnswerOption>()
                .in(ReportAnswerOption.ANSWER_ID, reportAnswerIds)
                .groupBy(ReportAnswerOption.QUESTION_ID);
        List<ReportAnswerOption> reportAnswerOptionGroupList = reportAnswerOptionService.selectList(reportAnswerOptionWrapper);

        Wrapper<ReportAnswerOption> wrapper = new EntityWrapper<ReportAnswerOption>()
                .in(ReportAnswerOption.ANSWER_ID, reportAnswerIds);
        List<ReportAnswerOption> reportAnswerOptionList = reportAnswerOptionService.selectList(wrapper);
//        Map<Integer, List<ReportAnswerOption>> questionMap = reportAnswerOptionList.stream().collect(Collectors.groupingBy(ReportAnswerOption::getQuestionId));

        String hospitalName = getHospitalNameById(reportAnswerList.get(0).getHospitalId());
        String hospitalOfficeName = getOfficeNameById(param.getOfficeId());

        String path = "temp";
        initPath(path);
        String fileName = System.currentTimeMillis() +".xls";

        try {
            File file = new File(path + "/" + fileName);
            WritableWorkbook wb = jxl.Workbook.createWorkbook(file);
            // 改变默认颜色
            Color color = Color.decode("#EEA9B8");
            wb.setColourRGB(Colour.RED, color.getRed(), color.getGreen(), color.getBlue());
            WritableSheet sheet = wb.createSheet(hospitalName, 0);
            // 写表头
            WritableCellFormat format = new WritableCellFormat();
            format.setBackground(Colour.RED);

            int columnPos = 0;
            int linePos = 0;
            sheet.addCell(new Label(columnPos++, linePos, "调查员账号", format));
            sheet.addCell(new Label(columnPos++, linePos, "病人ID", format));
            sheet.addCell(new Label(columnPos++, linePos, "调查医院", format));
            sheet.addCell(new Label(columnPos++, linePos, "调查科室", format));
            sheet.addCell(new Label(columnPos++, linePos, "调查问卷", format));
            sheet.addCell(new Label(columnPos++, linePos, "调查开始", format));
            sheet.addCell(new Label(columnPos++, linePos, "调查结束", format));
            for (ReportAnswerOption answerOption : reportAnswerOptionGroupList) {
                sheet.addCell(new Label(columnPos++, linePos, answerOption.getQuestionName(), format));
            }

            // 列宽度
            for (int i = 0; i < columnPos; i++) {
                sheet.setColumnView(i, 18);
            }

            for (ReportAnswer answer : reportAnswerList) {
                columnPos = 0;
                linePos++;
                sheet.addCell(new Label(columnPos++, linePos, getUserNameById(answer.getCreateId())));
                sheet.addCell(new Label(columnPos++, linePos, answer.getPatientNumber()));
                sheet.addCell(new Label(columnPos++, linePos, hospitalName));
                sheet.addCell(new Label(columnPos++, linePos, hospitalOfficeName));
                sheet.addCell(new Label(columnPos++, linePos, getQuestionnaireNameById(answer.getQuestionnaireId())));
                sheet.addCell(new Label(columnPos++, linePos, FormatUtil.dateTime(answer.getStartTime())));
                sheet.addCell(new Label(columnPos++, linePos, FormatUtil.dateTime(answer.getEndTime())));

                for (ReportAnswerOption answerOption : reportAnswerOptionGroupList) {
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

    private String getOfficeNameById(int officeId) {
        Wrapper<HospitalOffice> wrapper = new EntityWrapper<HospitalOffice>()
                .eq(HospitalOffice.ID, officeId);
        HospitalOffice row = hospitalOfficeService.selectOne(wrapper);
        if (Objects.nonNull(row)) {
            return row.getTitle();
        }
        return "";
    }

    private String getQuestionnaireNameById(int questionnaireId) {
        Wrapper<Questionnaire> wrapper = new EntityWrapper<Questionnaire>()
                .eq(Questionnaire.ID, questionnaireId);
        Questionnaire row = questionnaireService.selectOne(wrapper);
        if (Objects.nonNull(row)) {
            return row.getTitle();
        }
        return "";
    }

    private String getUserNameById(int userId) {
        if (userId <= 0) {
            return "无";
        }
        Wrapper<User> wrapper = new EntityWrapper<User>()
                .eq(User.ID, userId);
        User row = userService.selectOne(wrapper);
        if (Objects.nonNull(row)) {
            return row.getName();
        }
        return "";
    }
}

package com.sunchs.lyt.item.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.sunchs.lyt.db.business.entity.*;
import com.sunchs.lyt.db.business.service.impl.*;
import com.sunchs.lyt.framework.enums.OfficeTypeEnum;
import com.sunchs.lyt.framework.util.UserThreadUtil;
import com.sunchs.lyt.item.bean.InputAnswerBean;
import com.sunchs.lyt.item.exception.ItemException;
import com.sunchs.lyt.item.service.IItemFileService;
import jxl.Cell;
import jxl.LabelCell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.format.Colour;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ItemFileService implements IItemFileService {

    @Autowired
    private ItemOfficeServiceImpl itemOfficeService;
    @Autowired
    private QuestionnaireExtendServiceImpl questionnaireExtendService;
    @Autowired
    private QuestionServiceImpl questionService;
    @Autowired
    private ItemServiceImpl itemService;
    @Autowired
    private QuestionOptionServiceImpl questionOptionService;
    @Autowired
    private AnswerServiceImpl answerService;
    @Autowired
    private AnswerOptionServiceImpl answerOptionService;

    @Override
    public String getItemAnswerInputTemplate(Integer itemId, Integer officeType, Integer officeId) {
        // 查询科室绑定数据
        Wrapper<ItemOffice> itemOfficeWrapper = new EntityWrapper<ItemOffice>()
                .eq(ItemOffice.ITEM_ID, itemId)
                .eq(ItemOffice.OFFICE_TYPE_ID, officeType)
                .eq(ItemOffice.OFFICE_ID, officeId);
        ItemOffice itemOffice = itemOfficeService.selectOne(itemOfficeWrapper);
        if (Objects.isNull(itemOffice)) {
            throw new ItemException("项目和科室无绑定状态，请重新选择项目和科室！");
        }
        // 判断是否有问卷
        if (itemOffice.getQuestionnaireId().equals(0)) {
            throw new ItemException("无绑定问卷，请绑定问卷再试！");
        }
        // 查询问卷题目
        Wrapper<QuestionnaireExtend> questionnaireExtendWrapper = new EntityWrapper<QuestionnaireExtend>()
                .setSqlSelect(QuestionnaireExtend.QUESTION_ID.concat(" AS questionId"))
                .eq(QuestionnaireExtend.QUESTIONNAIRE_ID, itemOffice.getQuestionnaireId());
        List<QuestionnaireExtend> tempQuestionnaireExtendList = questionnaireExtendService.selectList(questionnaireExtendWrapper);
        List<Integer> questionIds = tempQuestionnaireExtendList.stream().map(QuestionnaireExtend::getQuestionId).collect(Collectors.toList());
        if (questionIds.size() == 0) {
            throw new ItemException("科室绑定的问卷没有题目，请绑定题目再试！");
        }
        Wrapper<Question> questionWrapper = new EntityWrapper<Question>()
                .in(Question.ID, questionIds);
        List<Question> questionList = questionService.selectList(questionWrapper);

        // 获取项目信息
        Wrapper<Item> itemWrapper = new EntityWrapper<Item>()
                .setSqlSelect(Item.TITLE)
                .eq(Item.ID, itemId);
        Item item = itemService.selectOne(itemWrapper);
        String itemName = item.getTitle();
        String officeTypeName = OfficeTypeEnum.get(officeType);
        String officeName = (officeType.equals(3) ? "员工" : itemOffice.getTitle());
        String sheetName = itemName + "-" + officeTypeName + "-" + officeName;


        // -------------------------
        String path = "/lyt";
        initPath(path);
        String fileName = System.currentTimeMillis() +".xls";

        new Thread(()->{
            try {
                File file = new File(path + "/" + fileName);
                WritableWorkbook wb = Workbook.createWorkbook(file);
                // 改变默认颜色
                Color color = Color.decode("#EEA9B8");
                wb.setColourRGB(Colour.RED, color.getRed(), color.getGreen(), color.getBlue());
                WritableSheet sheet = wb.createSheet(sheetName, 0);
                // 写表头
                WritableCellFormat format = new WritableCellFormat();
                format.setBackground(Colour.RED);

                int columnPos = 0;
                int linePos = 0;
                sheet.addCell(new Label(columnPos++, linePos, "患者编号", format));
                sheet.addCell(new Label(columnPos++, linePos, "调查开始", format));
                sheet.addCell(new Label(columnPos++, linePos, "调查结束", format));
                for (Question question : questionList) {
                    sheet.addCell(new Label(columnPos++, linePos, question.getTitle(), format));
                }

                // 列宽度
                for (int i = 0; i < columnPos; i++) {
                    sheet.setColumnView(i, 18);
                }

                wb.write();
                wb.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        return path + "/" + fileName;
    }

    @Override
    public String inputItemAnswer(Integer itemId, Integer officeType, Integer officeId, String file) {
        // 查询科室绑定数据
        Wrapper<ItemOffice> itemOfficeWrapper = new EntityWrapper<ItemOffice>()
                .eq(ItemOffice.ITEM_ID, itemId)
                .eq(ItemOffice.OFFICE_TYPE_ID, officeType)
                .eq(ItemOffice.OFFICE_ID, officeId);
        ItemOffice itemOffice = itemOfficeService.selectOne(itemOfficeWrapper);
        if (Objects.isNull(itemOffice)) {
            throw new ItemException("项目和科室无绑定状态，请重新选择项目和科室！");
        }
        // 判断是否有问卷
        if (itemOffice.getQuestionnaireId().equals(0)) {
            throw new ItemException("无绑定问卷，请绑定问卷再试！");
        }
        // 查询问卷题目
        Wrapper<QuestionnaireExtend> questionnaireExtendWrapper = new EntityWrapper<QuestionnaireExtend>()
                .setSqlSelect(QuestionnaireExtend.QUESTION_ID.concat(" AS questionId"))
                .eq(QuestionnaireExtend.QUESTIONNAIRE_ID, itemOffice.getQuestionnaireId());
        List<QuestionnaireExtend> tempQuestionnaireExtendList = questionnaireExtendService.selectList(questionnaireExtendWrapper);
        List<Integer> questionIds = tempQuestionnaireExtendList.stream().map(QuestionnaireExtend::getQuestionId).collect(Collectors.toList());
        if (questionIds.size() == 0) {
            throw new ItemException("科室绑定的问卷没有题目，请绑定题目再试！");
        }
        Wrapper<Question> questionWrapper = new EntityWrapper<Question>()
                .in(Question.ID, questionIds);
        List<Question> questionList = questionService.selectList(questionWrapper);

        Wrapper<QuestionOption> questionOptionWrapper = new EntityWrapper<QuestionOption>()
                .setSqlSelect(
                        QuestionOption.ID,
                        QuestionOption.QUESTION_ID.concat(" AS questionId"),
                        QuestionOption.TITLE,
                        QuestionOption.SCORE
                )
                .in(QuestionOption.QUESTION_ID, questionIds);
        List<QuestionOption> tempQuestionOptionList = questionOptionService.selectList(questionOptionWrapper);


        // ----------------------------------------------------------------------------
        int patientIdIndex = 0;// 患者ID位置
        int startTimeIndex = 1;// 开始时间位置
        int endTimeIndex = 2;// 结束时间位置

        // 读取文件
        if (file.length() == 0) {
            throw new ItemException("请选择导入文件");
        }
        File filePath = new File(file);
        if ( ! filePath.exists()) {
            throw new ItemException("导入文件不存在，请重新上传");
        }

        Workbook workBook = null;
        try {
            workBook = Workbook.getWorkbook(filePath);
        } catch (Exception e) {
            throw new ItemException("上传的文件有问题，请重新上传！");
        }
        Sheet sheet = workBook.getSheet(0);
        int rowLen = sheet.getRows();
        if (rowLen < 2) {
            throw new ItemException("导入文件无数据，请重试！");
        }
        int columnLen = sheet.getColumns();

        Map<String, Integer> xlsQuestionPos = new HashMap<>();
        for (int col = 3; col < columnLen; col++) {
            Cell c1 = sheet.getCell(col,0);
            LabelCell label =(LabelCell)c1;
            String questionName = label.getString();
            xlsQuestionPos.put(questionName.trim(), col);
        }
        // ----------------------------------------------------------------------------
        // 题目结果，key:题目ID
        Map<Integer, InputAnswerBean> questionMap = new HashMap<>();
        for (Question question : questionList) {
            if (xlsQuestionPos.containsKey(question.getTitle().trim())) {
                InputAnswerBean bean = new InputAnswerBean();
                bean.setQuestion(question);
                bean.setPosition(xlsQuestionPos.get(question.getTitle().trim()));
                // 选项集合
                Map<String, QuestionOption> optionMap = new HashMap<>();
                List<QuestionOption> questionGroup = tempQuestionOptionList.stream().filter(v -> v.getQuestionId().equals(question.getId())).collect(Collectors.toList());
                for (QuestionOption option : questionGroup) {
                    optionMap.put(option.getTitle().trim(), option);
                }
                bean.setQuestionOptionMap(optionMap);
                questionMap.put(question.getId(), bean);
            } else {
                throw new ItemException("缺少题目："+question.getTitle().trim());
            }
        }

        // 患者集合，key:行号
        Map<Integer, String> patientMap = new HashMap<>();
        for (int line = 1; line < rowLen; line++) {
            try {
                Cell cell = sheet.getCell(patientIdIndex, line);
                String patientNo = cell.getContents();
                if (patientNo.length() == 0) {
                    throw new ItemException("患者编号有问题");
                }
                patientMap.put(line, patientNo);
            } catch (Exception e) {
                throw new ItemException("患者编号有问题： (第"+(line+1)+"行，第"+(patientIdIndex+1)+"列)");
            }
        }

        // 时间集合，key:行号
        Map<Integer, InputAnswerBean> timeMap = new HashMap<>();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (int line = 1; line < rowLen; line++) {
            InputAnswerBean bean = new InputAnswerBean();
            // 判断开始时间
            Cell sCell = sheet.getCell(startTimeIndex, line);
            String sTimeStr = sCell.getContents();
            try {
                Date startTime = dateFormat.parse(sTimeStr);
                bean.setStartTime(startTime);
            } catch (ParseException e) {
                throw new ItemException("答题开始时间："+ sTimeStr +"(第"+(line+1)+"行，第"+(startTimeIndex+1)+"列)");
            }
            // 判断结束时间
            Cell eCell = sheet.getCell(endTimeIndex, line);
            String eTimeStr = eCell.getContents();
            try {
                Date endTime = dateFormat.parse(eTimeStr);
                bean.setEndTime(endTime);
            } catch (ParseException e) {
                throw new ItemException("答题结束时间："+ eTimeStr +"(第"+(line+1)+"行，第"+(endTimeIndex+1)+"列)");
            }
            timeMap.put(line, bean);
        }

        // 答案集合，key:行号
        Map<Integer, List<InputAnswerBean>> answerMap = new HashMap<>();
        for (int line = 1; line < rowLen; line++) {
            List<InputAnswerBean> beanList = new ArrayList<>();
            for (InputAnswerBean bean : questionMap.values()) {
                try {
                    Cell cell = sheet.getCell(bean.getPosition(), line);
                    String optionVal = cell.getContents();
                    if (optionVal.length() > 0) {
                        Map<String, QuestionOption> optionMapTempGroup = bean.getQuestionOptionMap();
                        // 过滤掉填空题的判断
                        if ( ! optionMapTempGroup.containsKey("填空，无需填写") && ! optionMapTempGroup.containsKey(optionVal.trim())) {
                            if (optionVal.contains(",")) {
                                // 判断多选题
                                String[] split = optionVal.split(",");
                                List<String> splitList = Arrays.asList(split);
                                for (String s : splitList) {
                                    if ( ! optionMapTempGroup.containsKey(s)) {
                                        throw new ItemException("题目的答案不存在："+s);
                                    }
                                }
                            } else {
                                throw new ItemException("题目的答案不存在："+optionVal);
                            }
                        }
                    }
                    bean.setOptionValue(optionVal);
                    beanList.add(bean);
                } catch (ItemException e) {
                    throw new ItemException(e.getMessage()+"(第"+(line+1)+"行，第"+(bean.getPosition()+1)+"列)[题目ID："+bean.getQuestion().getId()+"]");
                } catch (Exception e) {
                    throw new ItemException("题目的答案有问题：(第"+(line+1)+"行，第"+(bean.getPosition()+1)+"列)");
                }
            }
            answerMap.put(line, beanList);
        }

                // 写入数据
        new Thread(()-> {
            for (int line = 1; line < rowLen; line++) {
                for (InputAnswerBean bean : questionMap.values()) {
                    if ("在进行检验检查时，检验/检查人员态度友善，能够清晰说明检查步骤和需要注意的问题。".equals(bean.getQuestion().getTitle())) {
                        System.out.println(line+"|->"+bean.getQuestionOptionMap().values());
                        System.out.println(bean.getOptionValue());
                    }
                }
            }
        }).start();

//        // 写入数据
//        new Thread(()->{
//            for (int line = 1; line < rowLen; line++) {
//                // 提取时间
//                InputAnswerBean tBean = timeMap.get(line);
//                Date st = tBean.getStartTime();
//                Date et = tBean.getEndTime();
//                double lt = (double)(et.getTime() - st.getTime()) / (double)1000;
//
//                Answer answer = new Answer();
//                answer.setHospitalId(itemOffice.getHospitalId());
//                answer.setItemId(itemOffice.getItemId());
//                answer.setOfficeTypeId(itemOffice.getOfficeTypeId());
//                answer.setOfficeId(itemOffice.getOfficeId());
//                answer.setQuestionnaireId(itemOffice.getQuestionnaireId());
//                answer.setUserId(UserThreadUtil.getUserId());
//                answer.setMemberId(0);
//                answer.setPatientNumber(patientMap.get(line));
//                answer.setStatus(0);
//                answer.setReason("");
//                answer.setTimeDuration((int)lt);
//                answer.setStartTime(st);
//                answer.setEndTime(et);
//                answer.setUpdateId(UserThreadUtil.getUserId());
//                answer.setUpdateTime(new Date());
//                answer.setCreateId(UserThreadUtil.getUserId());
//                answer.setCreateTime(new Date());
//                answer.setFilterReason("");
//                if (answerService.insert(answer)) {
//                    for (InputAnswerBean bean : questionMap.values()) {
//                        // 有值时插入
//                        if (bean.getOptionValue().length() > 0) {
//                            Map<String, QuestionOption> optionMapTempGroup = bean.getQuestionOptionMap();
//                            // 填空题
//                            if (optionMapTempGroup.containsKey("填空，无需填写")) {
//                                setAnswerOption(answer, bean, 0, bean.getOptionValue().trim(), 0);
//                            } else if (optionMapTempGroup.containsKey(bean.getOptionValue().trim())) {
//                                QuestionOption option = optionMapTempGroup.get(bean.getOptionValue().trim());
//                                setAnswerOption(answer, bean, option.getId(), bean.getOptionValue().trim(), option.getScore());
//                            } else if (bean.getOptionValue().contains(",")) {
//                                // 判断多选题
//                                String[] split = bean.getOptionValue().split(",");
//                                List<String> splitList = Arrays.asList(split);
//                                for (String s : splitList) {
//                                    if (optionMapTempGroup.containsKey(s)) {
//                                        QuestionOption option = optionMapTempGroup.get(s);
//                                        setAnswerOption(answer, bean, option.getId(), s.trim(), option.getScore());
//                                    }
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//        }).start();
        return "恭喜，成功插入"+(rowLen-1)+"份答卷";
    }

    private void setAnswerOption(Answer answer, InputAnswerBean bean, Integer optionId, String optionValue, Integer score) {
        Question question = bean.getQuestion();

        AnswerOption answerOption = new AnswerOption();
        answerOption.setAnswerId(answer.getId());
        answerOption.setItemId(answer.getItemId());
        answerOption.setOfficeTypeId(answer.getOfficeTypeId());
        answerOption.setOfficeId(answer.getOfficeId());
        answerOption.setQuestionnaireId(answer.getQuestionnaireId());
        answerOption.setQuestionId(question.getId());
        answerOption.setQuestionName(question.getTitle());
        answerOption.setOptionId(optionId);
        answerOption.setOptionName(optionValue);
        //--持续时间待优化-------
        answerOption.setTimeDuration(answer.getTimeDuration());
        answerOption.setStartTime(answer.getStartTime());
        answerOption.setEndTime(answer.getEndTime());
        //---------
        answerOption.setTargetOne(question.getTargetOne());
        answerOption.setTargetTwo(question.getTargetTwo());
        answerOption.setTargetThree(question.getTargetThree());
        answerOption.setOptionType(question.getOptionType());
        answerOption.setScore(score);
        answerOptionService.insert(answerOption);
    }

    @Override
    public String uploadFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new ItemException("请选择导入文件");
        }

        String fileName = "/lyt";
        File fileUpload = new File(fileName);
        if (!fileUpload.exists()) {
            fileUpload.mkdirs();
        }

        fileName += "/upload_"+System.currentTimeMillis()+file.getOriginalFilename();
        fileUpload = new File(fileName);

        try {
            file.transferTo(fileUpload);
        } catch (IOException e) {
            throw new ItemException("上传文件到服务器失败：" + e.toString());
        }

        return fileName;
    }

    private void initPath(String path) {
        File file1 = new File(path);
        if ( ! file1.exists()) {
            file1.mkdirs();
        }
    }
}

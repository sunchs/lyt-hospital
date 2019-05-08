package com.sunchs.lyt.question.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.sunchs.lyt.db.business.entity.Questionnaire;
import com.sunchs.lyt.db.business.entity.QuestionnaireExtend;
import com.sunchs.lyt.framework.bean.PagingList;
import com.sunchs.lyt.framework.util.JsonUtil;
import com.sunchs.lyt.framework.util.NumberUtil;
import com.sunchs.lyt.framework.util.ObjectUtil;
import com.sunchs.lyt.framework.util.PagingUtil;
import com.sunchs.lyt.question.bean.*;
import com.sunchs.lyt.question.dao.QuestionDao;
import com.sunchs.lyt.question.dao.QuestionnaireDao;
import com.sunchs.lyt.question.dao.ipml.QuestionOptionDaoImpl;
import com.sunchs.lyt.question.exception.QuestionException;
import com.sunchs.lyt.question.service.IQuestionnaireService;
import jxl.format.Colour;
import jxl.write.Label;
import jxl.write.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;
import java.util.*;

@Service
public class QuestionnaireService implements IQuestionnaireService {

    @Autowired
    private QuestionOptionDaoImpl questionOptionDao;

    @Autowired
    private QuestionnaireDao questionnaireDao;

    @Autowired
    private QuestionDao questionDao;

    @Override
    public QuestionnaireData getById(int id) {
        Questionnaire res = questionnaireDao.getById(id);
        if (Objects.nonNull(res)) {
            QuestionnaireData data = ObjectUtil.copy(res, QuestionnaireData.class);
            data.initData();

            List<QuestionDataExt> questionList = new ArrayList<>();
            questionnaireDao.getExtendById(id).forEach(ext->{
                QuestionData questionData = questionDao.getById(ext.getQuestionId());
                if (ext.getSkipMode().equals(1)) {
                    questionData.getOption().forEach(o -> {
                        o.setSkipQuestionId(ext.getSkipQuestionId());
                    });
                } else if (ext.getSkipMode().equals(2)) {
                    Map<Integer, Integer> skipMap = getSkipContentMap(ext.getSkipContent());
                    questionData.getOption().forEach(o -> {
                        if (skipMap.containsKey(o.getOptionId())) {
                            o.setSkipQuestionId(skipMap.get(o.getOptionId()));
                        }
                    });
                }

                QuestionDataExt extData = ObjectUtil.copy(questionData, QuestionDataExt.class);
                extData.setSkipMode(ext.getSkipMode());
                if (Objects.nonNull(questionData)) {
                    questionList.add(extData);
                }
            });

            data.setQuestionList(questionList);
            return data;
        }
        return null;
    }

    @Override
    public PagingList<Questionnaire> getPageList(QuestionnaireParam param) {
        Wrapper<Questionnaire> where = new EntityWrapper<>();
        Page<Questionnaire> pageData = questionnaireDao.getPaging(where, param.getPageNow(), param.getPageSize());
        return PagingUtil.getData(pageData);
    }

    @Override
    public void save(QuestionnaireParam param) {
        if (NumberUtil.isZero(param.getId())) {
            insert(param);
        } else {
            update(param);
        }
    }

    @Override
    public String createExcelFile(int id) {
        String path = "temp";
        String fileName = System.currentTimeMillis() +".xls";
        initPath(path);

        QuestionnaireData data = getById(id);
        if (Objects.isNull(data)) {
            throw new QuestionException("找不到问卷信息");
        }
        try {
            File file = new File(path + "/" + fileName);
            WritableWorkbook wb = jxl.Workbook.createWorkbook(file);
            // 改变默认颜色
            Color color = Color.decode("#EEA9B8");
            wb.setColourRGB(Colour.RED, color.getRed(), color.getGreen(), color.getBlue());
            WritableSheet sheet = wb.createSheet(data.getTitle(), 0);
            // 写表头
            WritableCellFormat format = new WritableCellFormat();
            format.setBackground(Colour.RED);
            // 标签列数
            int tagLen = getTagLen(data.getQuestionList());
            // 选项列数
            int optionLen = getOptionLen(data.getQuestionList());

            int columnPos = 0;
            int linePos = 0;
            sheet.addCell(new Label(columnPos++, linePos, "题目编号", format));
            sheet.addCell(new Label(columnPos++, linePos, "题目类型", format));
            sheet.addCell(new Label(columnPos++, linePos, "问题文本", format));
            sheet.addCell(new Label(columnPos++, linePos, "跳转题目", format));
            sheet.addCell(new Label(columnPos++, linePos, "三级指标", format));
            sheet.addCell(new Label(columnPos++, linePos, "二级指标", format));
            for (int i = 0; i < tagLen; i++) {
                sheet.addCell(new Label(columnPos++, linePos, "标签"+(i+1), format));
            }
            for (int i = 0; i < optionLen; i++) {
                sheet.addCell(new Label(columnPos++, linePos, "答案"+(i+1), format));
            }
            // 列宽度
            for (int i = 0; i < columnPos; i++) {
                sheet.setColumnView(i, 12);
            }
            sheet.setColumnView(0, 10);
            sheet.setColumnView(2, 40);

            // 写入数据
            for (QuestionDataExt question : data.getQuestionList()) {
                columnPos = 0;
                linePos++;

                sheet.addCell(new Label(columnPos++, linePos, question.getNumber()));
                sheet.addCell(new Label(columnPos++, linePos, question.getOptionTypeName()));
                sheet.addCell(new Label(columnPos++, linePos, question.getTitle()));
                sheet.addCell(new Label(columnPos++, linePos, getSkipContent(question)));
                sheet.addCell(new Label(columnPos++, linePos, question.getTargetThreeName()));
                sheet.addCell(new Label(columnPos++, linePos, question.getTargetTwoName()));
                // 自动标签
                List<TagData> tagList = question.getTagList();
                for (int i = 0; i < tagList.size(); i++) {
                    sheet.addCell(new Label(columnPos+i, linePos, tagList.get(i).getTagName()));
                }
                columnPos += tagLen;
                // 自动选项
                List<OptionData> optionList = question.getOption();
                for (int i = 0; i < optionList.size(); i++) {
                    sheet.addCell(new Label(columnPos+i, linePos, optionList.get(i).getOptionName()));
                }
                columnPos += optionLen;
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

    private int getTagLen(List<QuestionDataExt> questionList) {
        int len = 0;
        for (QuestionDataExt ext : questionList) {
            int size = ext.getTagList().size();
            if (size > len) {
                len = size;
            }
        }
        return len;
    }

    private int getOptionLen(List<QuestionDataExt> questionList) {
        int len = 0;
        for (QuestionDataExt ext : questionList) {
            int size = ext.getOption().size();
            if (size > len) {
                len = size;
            }
        }
        return len;
    }

    private String getSkipContent(QuestionDataExt questionDataExt) {
        if (questionDataExt.getSkipMode() == 1) {
            for (OptionData optionData : questionDataExt.getOption()) {
                return optionData.getSkipQuestionId()+"";
            }
        } else if (questionDataExt.getSkipMode() == 2) {
            String val = "";
            for (OptionData optionData : questionDataExt.getOption()) {
                if (optionData.getSkipQuestionId() != 0) {
                    String item = optionData.getOptionId()+":"+optionData.getSkipQuestionId();
                    val += val.isEmpty() ? item : ", "+item;
                }
            }
            return val;
        }
        return "";
    }

    private void initPath(String path) {
        File file1 = new File(path);
        if ( ! file1.exists()) {
            file1.mkdirs();
        }
    }

    private void insert(QuestionnaireParam param) {
        Questionnaire data = new Questionnaire();
        // TODO::医院ID
        data.setHospitalId(0);
        data.setTitle(param.getTitle());
        data.setStatus(param.getStatus());
        data.setTargetOne(param.getTargetOne());
        // TODO::用户ID
        data.setUpdateId(0);
        data.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        // TODO::用户ID
        data.setCreateId(0);
        data.setCreateTime(new Timestamp(System.currentTimeMillis()));
        if (questionnaireDao.insert(data)) {
            Integer questionnaireId = data.getId();
            List<QuestionBean> questionList = param.getQuestion();
            for (QuestionBean question : questionList) {
                QuestionnaireExtend extend = new QuestionnaireExtend();
                extend.setQuestionnaireId(questionnaireId);
                extend.setQuestionId(question.getQuestionId());
                extend.setSkipMode(question.getSkipMode());
                if (question.getSkipMode() == 1) {
                    extend.setSkipQuestionId(question.getSkipQuestionId());
                } else if (question.getSkipMode() == 2) {
                    String skipBody = setSkipContent(question.getQuestionId(), question.getSkipContent());
                    extend.setSkipContent(skipBody);
                }
                questionnaireDao.insertQuestion(extend);
            }
        }
    }

    /**
     * 构建 选项跳转 的字符串
     */
    private String setSkipContent(int questionId, List<OptionSkipParam> skipList) {
        List<OptionSkipParam> skipContent = new ArrayList<>();
        List<OptionData> optionList = questionOptionDao.getListById(questionId);
        if (optionList.size() > 0) {
            for (OptionData option : optionList) {
                OptionSkipParam opt = new OptionSkipParam();
                opt.setOptionId(option.getOptionId());
                opt.setSkipQuestionId(setSkipQuestionId(option, skipList));
                skipContent.add(opt);
            }
        }
        return JsonUtil.toJson(skipContent);
    }

    /**
     * 提取有效跳转题目ID
     */
    private int setSkipQuestionId(OptionData option, List<OptionSkipParam> skipList) {
        if (Objects.nonNull(skipList) && skipList.size() > 0) {
            for (OptionSkipParam skip : skipList) {
                if (skip.getOptionId() == option.getOptionId()) {
                    return skip.getSkipQuestionId();
                }
            }
        }
        return 0;
    }

    private void update(QuestionnaireParam param) {
        Questionnaire data = new Questionnaire();
        data.setId(param.getId());
        data.setStatus(param.getStatus());
        // TODO::用户ID
        data.setUpdateId(0);
        data.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        questionnaireDao.update(data);
    }

    private Map<Integer, Integer> getSkipContentMap(String skipContent) {
        Map<Integer, Integer> skipMap = new HashMap<>();
        JSONArray objects = JSONObject.parseArray(skipContent);
        for (int i = 0; i < objects.size(); i++) {
            skipMap.put(objects.getJSONObject(i).getInteger("optionId"), objects.getJSONObject(i).getInteger("skipQuestionId"));
        }
        return skipMap;
    }
}
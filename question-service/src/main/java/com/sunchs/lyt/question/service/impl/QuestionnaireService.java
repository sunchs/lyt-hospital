package com.sunchs.lyt.question.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.sunchs.lyt.db.business.entity.Hospital;
import com.sunchs.lyt.db.business.entity.Questionnaire;
import com.sunchs.lyt.db.business.entity.QuestionnaireExtend;
import com.sunchs.lyt.db.business.service.impl.HospitalServiceImpl;
import com.sunchs.lyt.db.business.service.impl.QuestionnaireExtendServiceImpl;
import com.sunchs.lyt.db.business.service.impl.QuestionnaireServiceImpl;
import com.sunchs.lyt.framework.bean.PagingList;
import com.sunchs.lyt.framework.util.*;
import com.sunchs.lyt.question.bean.*;
import com.sunchs.lyt.question.dao.QuestionDao;
import com.sunchs.lyt.question.dao.QuestionTargetDao;
import com.sunchs.lyt.question.dao.QuestionnaireDao;
import com.sunchs.lyt.question.dao.ipml.QuestionOptionDaoImpl;
import com.sunchs.lyt.question.enums.QuestionnaireStatusEnum;
import com.sunchs.lyt.question.exception.QuestionException;
import com.sunchs.lyt.question.service.IQuestionnaireService;
import jxl.format.Colour;
import jxl.write.Label;
import jxl.write.*;
import org.apache.commons.beanutils.PropertyUtils;
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

    @Autowired
    private QuestionTargetDao questionTargetDao;

    @Autowired
    private QuestionnaireServiceImpl questionnaireService;

    @Autowired
    private QuestionnaireExtendServiceImpl questionnaireExtendService;

    @Autowired
    private HospitalServiceImpl hospitalService;

    @Override
    public QuestionnaireData getById(int id) {
        // 获取问卷基本信息
        Questionnaire res = questionnaireDao.getById(id);
        if (Objects.nonNull(res)) {
            QuestionnaireData data = ObjectUtil.copy(res, QuestionnaireData.class);
            data.initData();
            // 获取问卷扩展信息
            List<QuestionData> questionList = new ArrayList<>();
            questionnaireDao.getExtendById(id).forEach(ext->{
                // 获取题目详情
                QuestionData questionData = questionDao.getById(ext.getQuestionId());
                if (Objects.nonNull(questionData)) {
                    questionData.setSkipMode(ext.getSkipMode());
                    // 针对直接跳转
                    questionData.setSkipQuestionId(ext.getSkipQuestionId());
                    // 针对选项跳转
                    if (ext.getSkipMode().equals(2)) {
                        Map<Integer, Integer> skipMap = getSkipContentMap(ext.getSkipContent());
                        questionData.getOptionList().forEach(o -> {
                            if (skipMap.containsKey(o.getOptionId())) {
                                o.setSkipQuestionId(skipMap.get(o.getOptionId()));
                            }
                        });
                    }
                    // 题目排序
                    questionData.setSort(ext.getSort());
                    questionList.add(questionData);
                }
            });
            data.setQuestionList(questionList);
            return data;
        }
        return null;
    }

    @Override
    public PagingList<QuestionnaireData> getPageList(QuestionnaireParam param) {
        Wrapper<Questionnaire> where = new EntityWrapper<>();
        if (UserThreadUtil.getHospitalId() > 0) {
            where.eq(Questionnaire.HOSPITAL_ID, UserThreadUtil.getHospitalId());
        }
        where.orderBy(Questionnaire.ID, false);
        Page<Questionnaire> page = questionnaireService.selectPage(new Page<>(param.getPageNow(), param.getPageSize()), where);
        List<QuestionnaireData> list = new ArrayList<>();
        page.getRecords().forEach(row -> {
            QuestionnaireData data = ObjectUtil.copy(row, QuestionnaireData.class);
            data.setStatusName(QuestionnaireStatusEnum.get(row.getStatus()));
            data.setTargetOneName(questionTargetDao.getNameById(data.getTargetOne()));
            if (data.getHospitalId() == 0) {
                data.setHospitalName("通用");
            } else {
                Hospital hospital = hospitalService.selectById(data.getHospitalId());
                if (Objects.nonNull(hospital)) {
                    data.setHospitalName(hospital.getHospitalName());
                } else {
                    data.setHospitalName("绑定的医院不存在");
                }
            }
            data.setUpdateTimeName(FormatUtil.dateTime(data.getUpdateTime()));

            list.add(data);
        });


        return PagingUtil.getData(list, page.getTotal(), page.getCurrent(), page.getSize());
    }

    @Override
    public int save(QuestionnaireParam param) {
        if (NumberUtil.isZero(param.getId())) {
            return insert(param);
        } else {
            return update(param);
        }
    }

    @Override
    public void updateStatus(QuestionnaireParam param) {
        Questionnaire data = new Questionnaire();
        data.setId(param.getId());
        data.setStatus(param.getStatus());
        data.setUpdateId(UserThreadUtil.getUserId());
        data.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        questionnaireService.updateById(data);
    }

    @Override
    public List<Map<String, Object>> getUsableList(QuestionnaireParam param) {
        Wrapper<Questionnaire> wrapper = new EntityWrapper<Questionnaire>()
                .eq(Questionnaire.STATUS, 1);
        List<Map<String, Object>> list = new ArrayList<>();
        questionnaireService.selectList(wrapper).forEach(q -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", q.getId());
            map.put("title", q.getTitle());
            map.put("targetOne", q.getTargetOne());
            list.add(map);
        });
        return list;
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
            for (QuestionData question : data.getQuestionList()) {
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
                List<OptionData> optionList = question.getOptionList();
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

    @Override
    public List<QuestionnaireData> getDownloadList(QuestionnaireDownloadParam param) {
        if (Objects.isNull(param.getQuestionnaireIds())) {
            throw new QuestionException("请设置问卷ID集合");
        }
        if (param.getQuestionnaireIds().size() > 5) {
            throw new QuestionException("一次请求不能超过5问卷");
        }

        List<QuestionnaireData> list = new ArrayList<>();
        param.getQuestionnaireIds().forEach(id -> {
            QuestionnaireData data = getById(id);
            list.add(data);
        });
        return list;
    }

    private int getTagLen(List<QuestionData> questionList) {
        int len = 0;
        for (QuestionData ext : questionList) {
            int size = ext.getTagList().size();
            if (size > len) {
                len = size;
            }
        }
        return len;
    }

    private int getOptionLen(List<QuestionData> questionList) {
        int len = 0;
        for (QuestionData ext : questionList) {
            int size = ext.getOptionList().size();
            if (size > len) {
                len = size;
            }
        }
        return len;
    }

    private String getSkipContent(QuestionData questionData) {
        if (questionData.getSkipMode() == 1) {
            return questionData.getSkipQuestionId()+"";
        } else if (questionData.getSkipMode() == 2) {
            String val = "";
            for (OptionData optionData : questionData.getOptionList()) {
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

    private int insert(QuestionnaireParam param) {
        Questionnaire data = new Questionnaire();
        data.setHospitalId(UserThreadUtil.getHospitalId());
        data.setTitle(param.getTitle());
        data.setStatus(param.getStatus());
        data.setTargetOne(param.getTargetOne());
        data.setUpdateId(UserThreadUtil.getUserId());
        data.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        data.setCreateId(UserThreadUtil.getUserId());
        data.setCreateTime(new Timestamp(System.currentTimeMillis()));
        if (questionnaireDao.insert(data)) {
            insertExtend(data.getId(), param.getQuestionList());
        }
        return data.getId();
    }

    private int update(QuestionnaireParam param) {
        Questionnaire data = new Questionnaire();
        data.setId(param.getId());
        data.setTitle(param.getTitle());
        data.setUpdateId(UserThreadUtil.getUserId());
        data.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        if (questionnaireDao.update(data)) {
            insertExtend(data.getId(), param.getQuestionList());
        }
        return data.getId();
    }

    private void insertExtend(int questionnaireId, List<QuestionBean> questionList) {
        // 清楚历史数据
        Wrapper<QuestionnaireExtend> w = new EntityWrapper<>();
        w.eq(QuestionnaireExtend.QUESTIONNAIRE_ID, questionnaireId);
        questionnaireExtendService.delete(w);
        // 插入新数据
        for (QuestionBean question : questionList) {
            QuestionnaireExtend extend = new QuestionnaireExtend();
            extend.setQuestionnaireId(questionnaireId);
            extend.setQuestionId(question.getQuestionId());
            extend.setSkipMode(question.getSkipMode());
            extend.setSort(question.getSort());
            if (question.getSkipMode() == 1) {
                extend.setSkipQuestionId(question.getSkipQuestionId());
            } else if (question.getSkipMode() == 2) {
                String skipBody = setSkipContent(question.getQuestionId(), question.getOptionList());
                extend.setSkipContent(skipBody);
            }
            questionnaireDao.insertQuestion(extend);
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

    private Map<Integer, Integer> getSkipContentMap(String skipContent) {
        Map<Integer, Integer> skipMap = new HashMap<>();
        JSONArray objects = JSONObject.parseArray(skipContent);
        for (int i = 0; i < objects.size(); i++) {
            skipMap.put(objects.getJSONObject(i).getInteger("optionId"), objects.getJSONObject(i).getInteger("skipQuestionId"));
        }
        return skipMap;
    }
}
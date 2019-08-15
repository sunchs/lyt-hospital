package com.sunchs.lyt.item.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.sunchs.lyt.db.business.entity.*;
import com.sunchs.lyt.db.business.service.impl.*;
import com.sunchs.lyt.framework.bean.PagingList;
import com.sunchs.lyt.framework.util.*;
import com.sunchs.lyt.item.bean.AnswerData;
import com.sunchs.lyt.item.bean.AnswerImageData;
import com.sunchs.lyt.item.bean.AnswerOptionData;
import com.sunchs.lyt.item.bean.AnswerParam;
import com.sunchs.lyt.item.enums.AnswerStatusEnum;
import com.sunchs.lyt.item.service.IAnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AnswerService implements IAnswerService {

    @Autowired
    private AnswerServiceImpl answerService;

    @Autowired
    private AnswerImageServiceImpl answerImageService;

    @Autowired
    private AnswerOptionServiceImpl answerOptionService;

    @Autowired
    private ItemServiceImpl itemService;

    @Autowired
    private QuestionTagBindingServiceImpl questionTagBindingService;

    @Autowired
    private ReportAnswerServiceImpl reportAnswerService;

    @Autowired
    private ReportAnswerOptionServiceImpl reportAnswerOptionService;

    @Autowired
    private NamedParameterJdbcTemplate db;

    @Override
    public PagingList<AnswerData> getPageList(AnswerParam param) {
        Wrapper<Answer> wrapper = new EntityWrapper<>();
        if (param.getItemId() > 0) {
            wrapper.eq(Answer.ITEM_ID, param.getItemId());
        }
        if (param.getOfficeId() > 0) {
            wrapper.eq(Answer.OFFICE_ID, param.getOfficeId());
        }
        wrapper.orderBy(Answer.ID, false);
        Page<Answer> data = answerService.selectPage(new Page<>(param.getPageNow(), param.getPageSize()), wrapper);
        List<AnswerData> list = new ArrayList<>();
        data.getRecords().forEach(answer -> list.add(toAnswerData(answer)));
//        // 判断答卷之间的间隔
//        AnswerData tempAnswer = null;
//        for (AnswerData a : list) {
//            if (Objects.nonNull(tempAnswer)) {
//                long tVal = tempAnswer.getEndTime().getTime() - a.getStartTime().getTime();
//                if (tVal < 20 * 1000) {
//                    a.setWarningReason("答卷时间少于20秒");
//                } else {
//                    a.setWarningReason("");
//                }
//            } else {
//                a.setWarningReason("");
//            }
//            tempAnswer = a;
//        }
        return PagingUtil.getData(list, data.getTotal(), data.getCurrent(), data.getSize());
    }

    @Override
    public int save(AnswerParam param) {
//        data.setUpdateId(UserThreadUtil.getUserId());
//        data.setUpdateTime(new Timestamp(System.currentTimeMillis()));
//        data.setCreateId(UserThreadUtil.getUserId());
//        data.setCreateTime(new Timestamp(System.currentTimeMillis()));

        return 0;
    }

    @Override
    public void updateStatus(AnswerParam param) {
        if (param.getId() == 0) {
            param.getIdList().forEach(id -> {
                Answer data = new Answer();
                data.setId(id);
                data.setStatus(param.getStatus());
                if (StringUtil.isNotEmpty(param.getReason())) {
                    data.setReason(param.getReason());
                }
                data.setUpdateId(UserThreadUtil.getUserId());
                data.setUpdateTime(new Timestamp(System.currentTimeMillis()));
                if (answerService.updateById(data)) {
                    syncReportData(data.getId(), data.getStatus());
                }
            });
        } else {
            Answer data = new Answer();
            data.setId(param.getId());
            data.setStatus(param.getStatus());
            if (StringUtil.isNotEmpty(param.getReason())) {
                data.setReason(param.getReason());
            }
            data.setUpdateId(UserThreadUtil.getUserId());
            data.setUpdateTime(new Timestamp(System.currentTimeMillis()));
            if (answerService.updateById(data)) {
                syncReportData(data.getId(), data.getStatus());
            }
        }
    }

    @Override
    public void updateReason(AnswerParam param) {
        Answer data = new Answer();
        data.setId(param.getId());
        data.setReason(param.getReason());
        data.setUpdateId(UserThreadUtil.getUserId());
        data.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        answerService.updateById(data);
    }

    private void syncReportData(Integer answerId, Integer status) {
        reportAnswerService.deleteById(answerId);
        Wrapper<ReportAnswerOption> wrapper = new EntityWrapper<ReportAnswerOption>()
                .eq(ReportAnswerOption.ANSWER_ID, answerId);
        reportAnswerOptionService.delete(wrapper);

        if (status.equals(1)) {
            insertReportData(answerId);
        }
    }

    private void insertReportData(Integer answerId) {
        Answer answer = answerService.selectById(answerId);

        String sql = "INSERT INTO report_answer SELECT * FROM answer WHERE id=:answerId";
        MapSqlParameterSource param = new MapSqlParameterSource()
                .addValue("answerId", answerId);
        db.update(sql, param);

//        ReportAnswer data = new ReportAnswer();
//        data.setId(answer.getId());
//        data.setHospitalId(answer.getHospitalId());
//        data.setItemId(answer.getItemId());
//        data.setOfficeId(answer.getOfficeId());
//        data.setQuestionnaireId(answer.getQuestionnaireId());
//        data.setUserId(answer.getUserId());
//        data.setPatientNumber(answer.getPatientNumber());
//        data.setStatus(answer.getStatus());
//        data.setReason(answer.getReason());
//        data.setTimeDuration(answer.getTimeDuration());
//        data.setStartTime(answer.getStartTime());
//        data.setEndTime(answer.getEndTime());
//        data.setUpdateId(answer.getUpdateId());
//        data.setUpdateTime(answer.getUpdateTime());
//        data.setCreateId(answer.getCreateId());
//        data.setCreateTime(answer.getCreateTime());
//        data.setFilterReason(answer.getFilterReason());
//        if (reportAnswerService.insertOrUpdate(data)) {
            Wrapper<AnswerOption> answerOptionWrapper = new EntityWrapper<AnswerOption>()
                    .eq(AnswerOption.ANSWER_ID, answerId);
            List<AnswerOption> answerOptions = answerOptionService.selectList(answerOptionWrapper);
            answerOptions.forEach(row -> {
                ReportAnswerOption option = new ReportAnswerOption();
                option.setAnswerId(row.getAnswerId());
                option.setQuestionId(row.getQuestionId());
                option.setQuestionName(row.getQuestionName());
                option.setOptionId(row.getOptionId());
                option.setOptionName(row.getOptionName());
                option.setTimeDuration(row.getTimeDuration());
                option.setStartTime(row.getStartTime());
                option.setEndTime(row.getEndTime());
                reportAnswerOptionService.insert(option);
            });
//        }
    }

    private AnswerData toAnswerData(Answer answer) {
        AnswerData data = new AnswerData();
        data.setId(answer.getId());
        data.setHospitalId(answer.getHospitalId());
        data.setItemId(answer.getItemId());
        data.setOfficeId(answer.getOfficeId());
        data.setQuestionnaireId(answer.getQuestionnaireId());
        data.setUserId(answer.getUserId());
        data.setPatientNumber(answer.getPatientNumber());
        data.setStatus(answer.getStatus());
        data.setReason(answer.getReason());
        data.setTimeDuration(answer.getTimeDuration());
        data.setStartTime(answer.getStartTime());
        data.setEndTime(answer.getEndTime());
        data.setCreateTime(answer.getCreateTime());
        data.setFilterReason(answer.getFilterReason());

        data.setItemName(getItemName(answer.getItemId()));
        data.setStatusName(AnswerStatusEnum.get(answer.getStatus()));
        data.setStartTimeName(FormatUtil.dateTime(answer.getStartTime()));
        data.setEndTimeName(FormatUtil.dateTime(answer.getEndTime()));
        data.setCreateTimeName(FormatUtil.dateTime(answer.getCreateTime()));

        data.setQuestionOptionList(getQuestionOptionList(answer.getId()));
        data.setImageList(getImageList(answer.getId()));

        return data;
    }

    /**
     * 获取答卷图片
     */
    private List<AnswerImageData> getImageList(int answerId) {
        Wrapper<AnswerImage> wrapper = new EntityWrapper<AnswerImage>()
                .eq(AnswerImage.ANSWER_ID, answerId);
        List<AnswerImageData> imgList = new ArrayList<>();
        answerImageService.selectList(wrapper).forEach(img -> {
            AnswerImageData imageData = new AnswerImageData();
            if (img.getPath().indexOf("http://") == -1) {
                imageData.setPath("http://47.107.255.115" + img.getPath());
            } else {
                imageData.setPath(img.getPath());
            }
            imgList.add(imageData);
        });
        return imgList;
    }

    /**
     * 获取答卷题目选项
     */
    private List<AnswerOptionData> getQuestionOptionList(int answerId) {
//        Wrapper<AnswerOption> wrapper = new EntityWrapper<AnswerOption>()
//                .eq(AnswerOption.ANSWER_ID, answerId);
//        return answerOptionService.selectList(wrapper);

        List<AnswerOptionData> list = new ArrayList<>();
        Wrapper<AnswerOption> wrapper = new EntityWrapper<AnswerOption>()
                .eq(AnswerOption.ANSWER_ID, answerId);
        List<AnswerOption> optionList = answerOptionService.selectList(wrapper);

        // 题目ID集合
        List<Integer> qIds = new ArrayList<>();
        optionList.forEach(option -> {
            qIds.add(option.getQuestionId());
        });
        // 查询绑定的标签
        Wrapper<QuestionTagBinding> bindingWrapper = new EntityWrapper<QuestionTagBinding>()
                .in(QuestionTagBinding.QUESTION_ID, qIds);
        List<QuestionTagBinding> questionTagBindings = questionTagBindingService.selectList(bindingWrapper);

        // 转换对象
        optionList.forEach(option -> {
            AnswerOptionData data = ObjectUtil.copy(option, AnswerOptionData.class);

            List<Integer> tagIds = questionTagBindings.stream().filter(o -> o.getQuestionId().equals(option.getQuestionId()))
                    .map(QuestionTagBinding::getTagId).collect(Collectors.toList());
            data.setTagIds(tagIds);

            list.add(data);
        });

        // 判断同质问题
        list.forEach(row -> {
            row.setReason("");
            if (row.getTagIds().size() > 0) {
                list.forEach(son -> {
                    if ( ! son.getQuestionId().equals(row.getQuestionId())) {
                        Set<Integer> rowVal = new HashSet<>(row.getTagIds());
                        Set<Integer> sonVal = new HashSet<>(son.getTagIds());
                        rowVal.retainAll(sonVal);
                        if (rowVal.size() > 0 && ( ! son.getOptionName().equals(row.getOptionName()))) {
                            row.setReason("同质问题");
                        }
                    }
                });
            }
        });

        return list;
    }

    /**
     * 获取项目名称
     */
    private String getItemName(int itemId) {
        Wrapper<Item> wrapper = new EntityWrapper<Item>()
                .eq(Item.ID, itemId);
        Item item = itemService.selectOne(wrapper);
        if (Objects.nonNull(item)) {
            return item.getTitle();
        }
        return "";
    }
}

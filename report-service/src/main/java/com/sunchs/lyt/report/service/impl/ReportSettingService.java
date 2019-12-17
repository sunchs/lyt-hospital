package com.sunchs.lyt.report.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.sunchs.lyt.db.business.entity.Questionnaire;
import com.sunchs.lyt.db.business.entity.ReportAnswer;
import com.sunchs.lyt.db.business.service.impl.QuestionnaireServiceImpl;
import com.sunchs.lyt.db.business.service.impl.ReportAnswerOptionServiceImpl;
import com.sunchs.lyt.db.business.service.impl.ReportAnswerServiceImpl;
import com.sunchs.lyt.framework.bean.TitleData;
import com.sunchs.lyt.framework.util.ObjectUtil;
import com.sunchs.lyt.report.service.IReportSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class ReportSettingService implements IReportSettingService {

    @Autowired
    private ReportAnswerServiceImpl reportAnswerService;

    @Autowired
    private ReportAnswerOptionServiceImpl reportAnswerOptionService;

    @Autowired
    private QuestionnaireServiceImpl questionnaireService;

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
}

package com.sunchs.lyt.question.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.sunchs.lyt.db.business.entity.OptionTemplate;
import com.sunchs.lyt.db.business.entity.QuestionOption;
import com.sunchs.lyt.db.business.service.impl.OptionTemplateServiceImpl;
import com.sunchs.lyt.db.business.service.impl.QuestionOptionServiceImpl;
import com.sunchs.lyt.framework.bean.RequestData;
import com.sunchs.lyt.framework.bean.ResultData;
import com.sunchs.lyt.framework.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/postman")
public class PostmanController extends BaseController {

    @Autowired
    private QuestionOptionServiceImpl questionOptionService;

    @Autowired
    private OptionTemplateServiceImpl optionTemplateService;

    @PostMapping("/updateOptionTemp")
    public ResultData updateOptionTemp(@RequestBody RequestData data) {
        Wrapper<QuestionOption> wrapper = new EntityWrapper<>();
        wrapper.orderBy(QuestionOption.SORT, true);
        List<QuestionOption> optionList = questionOptionService.selectList(wrapper);
        Map<Integer, List<QuestionOption>> questionGroup = optionList.stream().collect(Collectors.groupingBy(QuestionOption::getQuestionId));
        for (Integer questionId : questionGroup.keySet()) {
            List<QuestionOption> questionOptionList = questionGroup.get(questionId);
            String value = "";
            for (QuestionOption option : questionOptionList) {
                value += value.equals("") ? option.getTitle() : ","+option.getTitle();
            }
            Wrapper<OptionTemplate> optionTemplateWrapper = new EntityWrapper<>();
            optionTemplateWrapper.eq(OptionTemplate.CONTENT, value);
            optionTemplateWrapper.eq(OptionTemplate.STATUS, 1);
            OptionTemplate optionTemplate = optionTemplateService.selectOne(optionTemplateWrapper);
            if (Objects.nonNull(optionTemplate)) {
                QuestionOption qOption = new QuestionOption();
                qOption.setTemplateId(optionTemplate.getId());
                // 更新模版ID
                Wrapper<QuestionOption> uWrapper = new EntityWrapper<>();
                uWrapper.eq(QuestionOption.QUESTION_ID, questionId);
                questionOptionService.update(qOption, uWrapper);
            }
        }
        return success();
    }

    @PostMapping("/updateOptionScore")
    public ResultData updateOptionScore(@RequestBody RequestData data) {
        Wrapper<QuestionOption> wrapper = new EntityWrapper<>();
        wrapper.orderBy(QuestionOption.SORT, true);
        List<QuestionOption> optionList = questionOptionService.selectList(wrapper);
        Map<Integer, List<QuestionOption>> questionGroup = optionList.stream().collect(Collectors.groupingBy(QuestionOption::getQuestionId));
        for (Integer questionId : questionGroup.keySet()) {
            List<QuestionOption> questionOptionList = questionGroup.get(questionId);
            String value = "";
            for (QuestionOption option : questionOptionList) {
                value += value.equals("") ? option.getTitle() : ","+option.getTitle();
            }
            Wrapper<OptionTemplate> optionTemplateWrapper = new EntityWrapper<>();
            optionTemplateWrapper.eq(OptionTemplate.CONTENT, value);
            optionTemplateWrapper.eq(OptionTemplate.STATUS, 1);
            OptionTemplate optionTemplate = optionTemplateService.selectOne(optionTemplateWrapper);
            if (Objects.nonNull(optionTemplate)) {
                questionOptionList.forEach(a->{
                    Integer index = a.getSort() - 1;
                    // 更新模版ID
                    String[] scoreArr = optionTemplate.getScore().split(",");
                    if (scoreArr.length == optionTemplate.getContent().split(",").length) {
                        QuestionOption qOption = new QuestionOption();
                        qOption.setScore(Integer.parseInt(scoreArr[index]));

                        Wrapper<QuestionOption> uWrapper = new EntityWrapper<>();
                        uWrapper.eq(QuestionOption.QUESTION_ID, a.getQuestionId());
                        uWrapper.eq(QuestionOption.SORT, a.getSort());
                        questionOptionService.update(qOption, uWrapper);
                    }
                });
            }
        }
        return success();
    }
}

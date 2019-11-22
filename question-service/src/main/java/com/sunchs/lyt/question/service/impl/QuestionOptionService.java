package com.sunchs.lyt.question.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.sunchs.lyt.db.business.entity.OptionTemplate;
import com.sunchs.lyt.db.business.service.impl.OptionTemplateServiceImpl;
import com.sunchs.lyt.framework.util.UserThreadUtil;
import com.sunchs.lyt.question.bean.*;
import com.sunchs.lyt.question.dao.ipml.QuestionOptionDaoImpl;
import com.sunchs.lyt.question.service.IQuestionOptionService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class QuestionOptionService implements IQuestionOptionService {

    @Autowired
    QuestionOptionDaoImpl questionOptionDao;

    @Autowired
    private OptionTemplateServiceImpl templateService;

    @Override
    public void save(QuestionOptionParam param) {
        OptionTemplate optionTemplate = new OptionTemplate();
        optionTemplate.setId(param.getId());
//        optionTemplate.setRemarks(param.getRemarks());
        if (questionOptionDao.update(optionTemplate)) {
            questionOptionDao.deleteOption(param.getId());
            List<OptionBean> optionList = param.getOptionList();
            for (OptionBean option : optionList) {
                questionOptionDao.insertOption(param.getId(), option.getOptionContent());
            }
        }
    }

    @Override
    public int saveTemplate(OptionTemplateParam param) {
        param.checkPid();
        param.checkOption();

        List<OptionTemplateOptionParam> oList = param.getOptionList();
        List<String> valueList = new ArrayList<>();
        List<Integer> scoreList = new ArrayList<>();
        oList.forEach(row->{
            valueList.add(row.getValue().trim());
            scoreList.add(row.getScore());
        });
        String value = StringUtils.join(valueList, ",");
        String score = StringUtils.join(scoreList, ",");
        OptionTemplate data = new OptionTemplate();
        data.setId(param.getId());
        data.setPid(param.getPid());
        data.setContent(value);
        data.setScore(score);
        data.setUpdateId(UserThreadUtil.getUserId());
        data.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        if (param.getId() == 0) {
            data.setCreateId(UserThreadUtil.getUserId());
            data.setCreateTime(new Timestamp(System.currentTimeMillis()));
        }
        if (templateService.insertOrUpdate(data)) {
            return data.getId();
        }
        return 0;
    }

    @Override
    public OptionTemplateData getTemplateById(int templateId) {
        Wrapper<OptionTemplate> w = new EntityWrapper<>();
        w.eq(OptionTemplate.ID, templateId);
        OptionTemplate res = templateService.selectOne(w);
        OptionTemplateData data = new OptionTemplateData();
        data.setId(res.getId());
        data.setPid(res.getPid());
//        data.setOptionList(Arrays.asList(res.getContent().split(",")));
        return data;
    }

    @Override
    public void updateTemplateStatus(OptionTemplateParam param) {
        OptionTemplate data = new OptionTemplate();
        data.setId(param.getId());
        data.setStatus(param.getStatus());
        data.setUpdateId(UserThreadUtil.getUserId());
        data.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        templateService.updateById(data);
    }

    @Override
    public QuestionOptionData getInfo(QuestionOptionParam param) {
        QuestionOptionData info = questionOptionDao.getInfo(param.getId());
        if (info != null) {
            info.setOptionList(questionOptionDao.getOptionList(param.getId()));
        }
        return info;
    }

    @Override
    public List<QuestionOptionData> getList(QuestionOptionParam param) {
        List<QuestionOptionData> typeList = questionOptionDao.getTypeList();
        for (QuestionOptionData type : typeList) {
            type.setOptionList(questionOptionDao.getOptionList(type.getId()));
        }
        return typeList;
    }

    @Override
    public List<Map<String, Object>> getCascaderData() {
        Wrapper<OptionTemplate> where = new EntityWrapper<>();
        where.ne(OptionTemplate.STATUS, 2);
        List<OptionTemplate> dbList = templateService.selectList(where);
        // 一级数据
        List<Map<String, Object>> oneList = fetchOptionList(dbList, 0);
        oneList.forEach(one -> {
            // 二级数据
            one.put("children", fetchOptionList(dbList, (int) one.get("id")));
        });
        return oneList;
    }

    private List<Map<String, Object>> fetchOptionList(List<OptionTemplate> dbList, Integer pid) {
        List<Map<String, Object>> list = new ArrayList<>();
        List<OptionTemplate> filterList = dbList.stream().filter(row -> row.getPid().equals(pid)).collect(Collectors.toList());
        for (OptionTemplate row : filterList) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", row.getId());
            map.put("title", row.getContent());
            list.add(map);
        }
        return list;
    }
}

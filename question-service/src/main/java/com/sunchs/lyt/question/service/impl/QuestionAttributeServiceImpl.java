package com.sunchs.lyt.question.service.impl;

import com.sunchs.lyt.framework.bean.PagingList;
import com.sunchs.lyt.framework.util.NumberUtil;
import com.sunchs.lyt.question.bean.QuestionAttributeData;
import com.sunchs.lyt.question.bean.QuestionAttributeParam;
import com.sunchs.lyt.question.bean.QuestionTargetData;
import com.sunchs.lyt.question.dao.QuestionAttributeDao;
import com.sunchs.lyt.question.exception.QuestionException;
import com.sunchs.lyt.question.service.QuestionAttributeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class QuestionAttributeServiceImpl implements QuestionAttributeService {

    @Autowired
    QuestionAttributeDao questionAttributeDao;

    @Override
    public QuestionAttributeData getById(Integer id) {
        return questionAttributeDao.getById(id);
    }

    @Override
    public PagingList<QuestionAttributeData> getList(Integer id) {
        PagingList<QuestionAttributeData> page = new PagingList<>();
        page.setTotal(questionAttributeDao.getCount(id));
        page.setList(questionAttributeDao.getList(id));
        return page;
    }

    @Override
    public QuestionAttributeData save(QuestionAttributeParam param) {
        Integer attrId = 0;
        if (NumberUtil.isZero(param.getId())) {
            attrId = insert(param);
        } else {
            attrId = update(param);
        }
        if (attrId > 0) {
            QuestionAttributeData attributeData = questionAttributeDao.getById(attrId);
            if (attributeData == null) {
                throw new QuestionException("属性ID：" + attrId + "，不存在");
            }
            return attributeData;
        }
        return null;
    }

    private Integer insert(QuestionAttributeParam param) {
        Map<String, Object> opt = new HashMap<>();
        opt.put("pid", param.getPid());
        opt.put("title", param.getTitle());
        opt.put("sort", param.getSort());
        Integer attrId = questionAttributeDao.insert(opt);
        if (attrId > 0) {
            List<QuestionAttributeParam> children = param.getChildren();
            for (QuestionAttributeParam child : children) {
                Map<String, Object> cMap = new HashMap<>();
                cMap.put("pid", attrId);
                cMap.put("title", child.getTitle());
                cMap.put("sort", child.getSort());
                questionAttributeDao.insert(cMap);
            }
        }
        return attrId;
    }

    private Integer update(QuestionAttributeParam param) {
        return 0;
    }

}

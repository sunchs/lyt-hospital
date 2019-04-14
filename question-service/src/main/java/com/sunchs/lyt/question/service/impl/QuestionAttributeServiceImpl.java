package com.sunchs.lyt.question.service.impl;

import com.sunchs.lyt.framework.bean.PagingList;
import com.sunchs.lyt.framework.util.NumberUtil;
import com.sunchs.lyt.question.bean.QuestionTagData;
import com.sunchs.lyt.question.bean.QuestionTagParam;
import com.sunchs.lyt.question.dao.QuestionTagDao;
import com.sunchs.lyt.question.exception.QuestionException;
import com.sunchs.lyt.question.service.QuestionAttributeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class QuestionAttributeServiceImpl implements QuestionAttributeService {

    @Autowired
    QuestionTagDao questionTagDao;

    @Override
    public QuestionTagData getById(Integer id) {
        return questionTagDao.getById(id);
    }

    @Override
    public PagingList<QuestionTagData> getList(Integer id) {
        PagingList<QuestionTagData> page = new PagingList<>();
        page.setTotal(questionTagDao.getCount(id));
        page.setList(questionTagDao.getList(id));
        return page;
    }

    @Override
    public QuestionTagData save(QuestionTagParam param) {
        Integer attrId = 0;
        if (NumberUtil.isZero(param.getId())) {
            attrId = insert(param);
        } else {
            attrId = update(param);
        }
        if (attrId > 0) {
            QuestionTagData attributeData = questionTagDao.getById(attrId);
            if (attributeData == null) {
                throw new QuestionException("属性ID：" + attrId + "，不存在");
            }
            return attributeData;
        }
        return null;
    }

    private Integer insert(QuestionTagParam param) {
        Map<String, Object> opt = new HashMap<>();
        opt.put("pid", param.getPid());
        opt.put("title", param.getTitle());
        opt.put("remarks", param.getRemarks());
        opt.put("updateId", 0);
        opt.put("updateTime", new Timestamp(System.currentTimeMillis()));
        opt.put("createId", 0);
        opt.put("createTime", new Timestamp(System.currentTimeMillis()));
        Integer attrId = questionTagDao.insert(opt);
        if (attrId > 0) {
            List<QuestionTagParam> children = param.getChildren();
            for (QuestionTagParam child : children) {
                Map<String, Object> cMap = new HashMap<>();
                cMap.put("pid", attrId);
                cMap.put("title", child.getTitle());
                cMap.put("remarks", "");
                cMap.put("updateId", 0);
                cMap.put("updateTime", new Timestamp(System.currentTimeMillis()));
                cMap.put("createId", 0);
                cMap.put("createTime", new Timestamp(System.currentTimeMillis()));
                questionTagDao.insert(cMap);
            }
        }
        return attrId;
    }

    private Integer update(QuestionTagParam param) {
        return 0;
    }

}
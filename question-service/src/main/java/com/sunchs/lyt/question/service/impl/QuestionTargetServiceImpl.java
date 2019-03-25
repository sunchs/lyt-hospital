package com.sunchs.lyt.question.service.impl;

import com.sunchs.lyt.framework.bean.PagingList;
import com.sunchs.lyt.framework.util.NumberUtil;
import com.sunchs.lyt.question.bean.QuestionTargetParam;
import com.sunchs.lyt.question.bean.QuestionTargetData;
import com.sunchs.lyt.question.dao.ipml.QuestionTargetDaoImpl;
import com.sunchs.lyt.question.exception.QuestionException;
import com.sunchs.lyt.question.service.QuestionTargetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class QuestionTargetServiceImpl implements QuestionTargetService {

    @Autowired
    QuestionTargetDaoImpl questionTargetDao;

    @Override
    public QuestionTargetData getById(Integer id) {
        QuestionTargetData target = questionTargetDao.getById(id);
        if (target == null) {
//            return new QuestionTargetData();
        }
        return target;
    }

    @Override
    public PagingList<QuestionTargetData> getList(Integer id) {
        PagingList<QuestionTargetData> page = new PagingList<>();
        page.setTotal(questionTargetDao.getCount(id));
        page.setList(questionTargetDao.getList(id));
        return page;
    }

    @Override
    public List<QuestionTargetData> getAll() {
        List<QuestionTargetData> dbList = questionTargetDao.getAll();
        for (QuestionTargetData one : dbList) {
            List<QuestionTargetData> twoList = dbList.stream().filter(row -> row.getPid() == one.getId()).collect(Collectors.toList());
            for (QuestionTargetData two : twoList) {
                List<QuestionTargetData> threeList = dbList.stream().filter(row -> row.getPid() == two.getId()).collect(Collectors.toList());
                two.setChildren(threeList);
            }
            one.setChildren(twoList);
        }
        return dbList.stream().filter(row -> row.getPid() == 0).collect(Collectors.toList());
    }

    @Override
    public QuestionTargetData save(QuestionTargetParam param) {
        Integer targetId = 0;
        if (NumberUtil.isZero(param.getId())) {
            targetId = insert(param);
        } else {
            targetId = update(param);
        }
        if (targetId > 0) {
            QuestionTargetData question = questionTargetDao.getById(targetId);
            if (question == null) {
                throw new QuestionException("指标ID：" + targetId + "，不存在");
            }
            return question;
        }
        return null;
    }

    private Integer insert(QuestionTargetParam param) {
        Map<String, Object> opt = new HashMap<>();
        opt.put("pid", param.getPid());
        opt.put("title", param.getTitle());
        opt.put("remarks", param.getRemarks());
        Integer targetId = questionTargetDao.insert(opt);
        if (targetId > 0) {
            List<QuestionTargetParam> children = param.getChildren();
            for (QuestionTargetParam child : children) {
                Map<String, Object> cMap = new HashMap<>();
                cMap.put("pid", targetId);
                cMap.put("title", child.getTitle());
                cMap.put("remarks", "");
                questionTargetDao.insert(cMap);
            }
        }
        return targetId;
    }

    private Integer update(QuestionTargetParam param) {
        return 0;
    }
}
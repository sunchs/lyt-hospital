package com.sunchs.lyt.question.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.sunchs.lyt.db.business.entity.QuestionTag;
import com.sunchs.lyt.db.business.service.impl.QuestionTagServiceImpl;
import com.sunchs.lyt.framework.bean.PagingList;
import com.sunchs.lyt.framework.util.NumberUtil;
import com.sunchs.lyt.question.bean.QuestionTagData;
import com.sunchs.lyt.question.bean.QuestionTagParam;
import com.sunchs.lyt.question.dao.QuestionTagDao;
import com.sunchs.lyt.question.exception.QuestionException;
import com.sunchs.lyt.question.service.IQuestionTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class QuestionTagService implements IQuestionTagService {

    @Autowired
    QuestionTagDao questionTagDao;

    @Autowired
    private QuestionTagServiceImpl tagService;

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

    @Override
    public List<Map<String, Object>> getCascaderData() {
        Wrapper<QuestionTag> where = new EntityWrapper<>();
        List<QuestionTag> dbList = tagService.selectList(where);
        // 一级数据
        List<Map<String, Object>> oneList = fetchTagList(dbList, 0);
        oneList.forEach(one -> {
            // 二级数据
            List<Map<String, Object>> twoList = fetchTagList(dbList, (int) one.get("id"));
            twoList.forEach(two -> {
                // 三级数据
                two.put("children", fetchTagList(dbList, (int) two.get("id")));
            });
            one.put("children", twoList);
        });
        return oneList;
    }

    private List<Map<String, Object>> fetchTagList(List<QuestionTag> dbList, Integer pid) {
        List<Map<String, Object>> list = new ArrayList<>();
        List<QuestionTag> filterList = dbList.stream().filter(row -> row.getPid().equals(pid)).collect(Collectors.toList());
        for (QuestionTag row : filterList) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", row.getId());
            map.put("title", row.getTitle());
            list.add(map);
        }
        return list;
    }
}
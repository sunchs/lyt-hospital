package com.sunchs.lyt.question.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.sunchs.lyt.db.business.entity.QuestionTag;
import com.sunchs.lyt.db.business.service.impl.QuestionTagServiceImpl;
import com.sunchs.lyt.framework.bean.PagingList;
import com.sunchs.lyt.framework.util.NumberUtil;
import com.sunchs.lyt.framework.util.UserThreadUtil;
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

    @Autowired
    private QuestionTagServiceImpl questionTagService;

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
    public int save(QuestionTagParam param) {
        if (NumberUtil.isZero(param.getId())) {
            return insert(param);
        } else {
            return update(param);
        }
    }

    private int insert(QuestionTagParam param) {
        QuestionTag data = new QuestionTag();
        data.setPid(0);
        data.setTitle(param.getTitle());
        data.setStatus(1);
        data.setUpdateId(UserThreadUtil.getUserId());
        data.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        data.setCreateId(UserThreadUtil.getUserId());
        data.setCreateTime(new Timestamp(System.currentTimeMillis()));
        if (questionTagService.insert(data)) {
            setChildren(data.getId(), param);
            return data.getId();
        }
        return 0;
    }

    private int update(QuestionTagParam param) {
        QuestionTag data = new QuestionTag();
        data.setId(param.getId());
        data.setTitle(param.getTitle());
        data.setStatus(1);
        data.setUpdateId(UserThreadUtil.getUserId());
        data.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        if (questionTagService.updateById(data)) {
            setChildren(data.getId(), param);
            return data.getId();
        }
        return 0;
    }

    private void setChildren(int id, QuestionTagParam param) {
        // 清理旧数据
        Wrapper<QuestionTag> w = new EntityWrapper<>();
        w.eq(QuestionTag.PID, id);
        questionTagService.delete(w);
        // 重新生产数据
        List<QuestionTagParam> children = param.getChildren();
        for (QuestionTagParam child : children) {
            QuestionTag son = new QuestionTag();
            son.setPid(id);
            son.setTitle(child.getTitle());
            son.setStatus(1);
            son.setUpdateId(UserThreadUtil.getUserId());
            son.setUpdateTime(new Timestamp(System.currentTimeMillis()));
            son.setCreateId(UserThreadUtil.getUserId());
            son.setCreateTime(new Timestamp(System.currentTimeMillis()));
            questionTagService.insert(son);
        }
    }

    @Override
    public List<Map<String, Object>> getCascaderData() {
        Wrapper<QuestionTag> where = new EntityWrapper<>();
        List<QuestionTag> dbList = tagService.selectList(where);
        // 一级数据
        List<Map<String, Object>> oneList = fetchTagList(dbList, 0);
        oneList.forEach(one -> {
            // 二级数据
            one.put("children", fetchTagList(dbList, (int) one.get("id")));
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
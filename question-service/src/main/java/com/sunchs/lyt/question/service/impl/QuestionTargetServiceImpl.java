package com.sunchs.lyt.question.service.impl;

import com.sunchs.lyt.db.business.entity.QuestionTarget;
import com.sunchs.lyt.framework.util.JsonUtil;
import com.sunchs.lyt.framework.util.NumberUtil;
import com.sunchs.lyt.framework.util.ObjectUtil;
import com.sunchs.lyt.framework.util.StringUtil;
import com.sunchs.lyt.question.bean.QuestionTargetData;
import com.sunchs.lyt.question.bean.QuestionTargetParam;
import com.sunchs.lyt.question.dao.ipml.QuestionTargetDaoImpl;
import com.sunchs.lyt.question.exception.QuestionException;
import com.sunchs.lyt.question.service.QuestionTargetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service("QuestionTargetServiceImpl1")
public class QuestionTargetServiceImpl implements QuestionTargetService {

    @Autowired
    QuestionTargetDaoImpl questionTargetDao;

    @Override
    public QuestionTargetData getById(int id) {
        return questionTargetDao.getById(id);
    }

//    @Override
//    public PagingList<QuestionTargetData> getList(int id) {
//        PagingList<QuestionTargetData> page = new PagingList<>();
//        page.setTotal(questionTargetDao.getCount(id));
//        page.setList(questionTargetDao.getList(id));
//        return page;
//    }

    @Override
    public List<QuestionTargetData> getAll() {
        List<QuestionTargetData> list = new ArrayList<>();
        List<QuestionTarget> dbList = questionTargetDao.getAll();
        for (QuestionTarget one : dbList) {
            QuestionTargetData oneData = ObjectUtil.copy(one, QuestionTargetData.class);
            oneData.initData();
            list.add(oneData);
        }

        for (QuestionTargetData one : list) {
            List<QuestionTargetData> twoList = list.stream().filter(row -> row.getPid().equals(one.getId())).collect(Collectors.toList());
            for (QuestionTargetData two : twoList) {
                List<QuestionTargetData> threeList = list.stream().filter(row -> row.getPid().equals(two.getId())).collect(Collectors.toList());
                two.setChildren(threeList);
            }
            one.setChildren(twoList);
        }
        return list.stream().filter(row -> row.getPid() == 0).collect(Collectors.toList());
    }

    @Override
    public List<Map<String, Object>> getSelectData() {
        List<QuestionTarget> dbList = questionTargetDao.getAll();
        // 一级数据
        List<Map<String, Object>> oneList = fetchTargetList(dbList, 0);
        oneList.forEach(one -> {
            // 二级数据
            List<Map<String, Object>> twoList = fetchTargetList(dbList, (int) one.get("id"));
            twoList.forEach(two -> {
                // 三级数据
                List<Map<String, Object>> threeList = fetchTargetList(dbList, (int) two.get("id"));
                two.put("children", threeList);
            });
            one.put("children", twoList);
        });
        return oneList;
    }

    private List<Map<String, Object>> fetchTargetList(List<QuestionTarget> dbList, Integer pid) {
        List<Map<String, Object>> list = new ArrayList<>();
        List<QuestionTarget> filterList = dbList.stream().filter(row -> row.getPid().equals(pid)).collect(Collectors.toList());
        for (QuestionTarget two : filterList) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", two.getId());
            map.put("title", two.getTitle());
            list.add(map);
        }
        return list;
    }

    @Override
    public void save(QuestionTargetParam param) {
        // 检查标题
        checkTitle(param.getTitle(), param.getPid());
        List<QuestionTargetParam> children = param.getChildren();
        for (QuestionTargetParam child : children) {
            checkTitle(child.getTitle(), param.getPid());
        }
        if (NumberUtil.isZero(param.getId())) {
            insert(param);
        } else {
//            update(param);
        }
    }

    private void insert(QuestionTargetParam param) {
        QuestionTarget data = new QuestionTarget();
        data.setPid(param.getPid());
        data.setTitle(param.getTitle());
        data.setStatus(1);
        data.setRemarks(param.getRemarks());
        // TODO::用户ID
        data.setUpdateId(0);
        data.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        // TODO::用户ID
        data.setCreateId(0);
        data.setCreateTime(new Timestamp(System.currentTimeMillis()));
        if (questionTargetDao.insert(data)) {
            List<QuestionTargetParam> children = param.getChildren();
            for (QuestionTargetParam child : children) {
                QuestionTarget childData = new QuestionTarget();
                childData.setPid(data.getId());
                childData.setTitle(child.getTitle());
                childData.setStatus(1);
                // TODO::用户ID
                childData.setUpdateId(0);
                childData.setUpdateTime(new Timestamp(System.currentTimeMillis()));
                // TODO::用户ID
                childData.setCreateId(0);
                childData.setCreateTime(new Timestamp(System.currentTimeMillis()));
                questionTargetDao.insert(childData);
            }
        }
    }

//    private Integer update(QuestionTargetParam param) {
//        return 0;
//    }

    private void checkTitle(String title, int target) {
        if (StringUtil.isEmpty(title)) {
            throw new QuestionException("指标标题不能为空");
        }
        int sonQty = questionTargetDao.titleQty(title, target);
        if (sonQty > 0) {
            throw new QuestionException("指标标题: [ "+ title +" ], 已存在");
        }
    }
}
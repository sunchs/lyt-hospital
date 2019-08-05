package com.sunchs.lyt.question.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.sunchs.lyt.db.business.entity.QuestionTarget;
import com.sunchs.lyt.db.business.service.impl.QuestionTargetServiceImpl;
import com.sunchs.lyt.framework.util.NumberUtil;
import com.sunchs.lyt.framework.util.ObjectUtil;
import com.sunchs.lyt.framework.util.StringUtil;
import com.sunchs.lyt.framework.util.UserThreadUtil;
import com.sunchs.lyt.question.bean.QuestionTargetData;
import com.sunchs.lyt.question.bean.QuestionTargetParam;
import com.sunchs.lyt.question.dao.ipml.QuestionTargetDaoImpl;
import com.sunchs.lyt.question.exception.QuestionException;
import com.sunchs.lyt.question.service.IQuestionTargetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service("QuestionTargetServiceImpl1")
public class QuestionTargetService implements IQuestionTargetService {

    @Autowired
    QuestionTargetDaoImpl questionTargetDao;

    @Autowired
    private QuestionTargetServiceImpl questionTargetService;

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
    public int save(QuestionTargetParam param) {
        // 检查标题
        if (NumberUtil.isZero(param.getId())) {
            checkTitle(param.getTitle(), param.getPid());
            List<QuestionTargetParam> children = param.getChildren();
            for (QuestionTargetParam child : children) {
                checkTitle(child.getTitle(), param.getPid());
            }
            return insert(param);
        } else {
            return update(param);
        }
    }

    @Override
    public void updateStatus(QuestionTargetParam param) {
        QuestionTarget data = new QuestionTarget();
        data.setId(param.getId());
        data.setStatus(param.getStatus());
        data.setUpdateId(UserThreadUtil.getUserId());
        data.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        questionTargetService.updateById(data);
    }

    @Override
    public List<Map<String, Object>> getCascaderData() {
        List<QuestionTarget> dbList = questionTargetDao.getAll();
        // 一级数据
        List<Map<String, Object>> oneList = fetchTargetList(dbList, 0);
        oneList.forEach(one -> {
            // 二级数据
            List<Map<String, Object>> twoList = fetchTargetList(dbList, (int) one.get("id"));
            twoList.forEach(two -> {
                // 三级数据
                two.put("children", fetchTargetList(dbList, (int) two.get("id")));
            });
            one.put("children", twoList);
        });
        return oneList;
    }

    private List<Map<String, Object>> fetchTargetList(List<QuestionTarget> dbList, Integer pid) {
        List<Map<String, Object>> list = new ArrayList<>();
        List<QuestionTarget> filterList = dbList.stream().filter(row -> row.getPid().equals(pid)).collect(Collectors.toList());
        for (QuestionTarget row : filterList) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", row.getId());
            map.put("title", row.getTitle());
            list.add(map);
        }
        return list;
    }

    private int insert(QuestionTargetParam param) {
        QuestionTarget data = new QuestionTarget();
        data.setPid(param.getPid());
        data.setTitle(param.getTitle());
        data.setStatus(1);
        data.setRemarks(param.getRemarks());
        data.setUpdateId(UserThreadUtil.getUserId());
        data.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        data.setCreateId(UserThreadUtil.getUserId());
        data.setCreateTime(new Timestamp(System.currentTimeMillis()));
        if (questionTargetDao.insert(data)) {
            List<QuestionTargetParam> children = param.getChildren();
            for (QuestionTargetParam child : children) {
                QuestionTarget childData = new QuestionTarget();
                childData.setPid(data.getId());
                childData.setTitle(child.getTitle());
                childData.setStatus(1);
                childData.setUpdateId(UserThreadUtil.getUserId());
                childData.setUpdateTime(new Timestamp(System.currentTimeMillis()));
                childData.setCreateId(UserThreadUtil.getUserId());
                childData.setCreateTime(new Timestamp(System.currentTimeMillis()));
                questionTargetDao.insert(childData);
            }
            return data.getId();
        }
        return 0;
    }

    private int update(QuestionTargetParam param) {
        QuestionTarget data = new QuestionTarget();
        data.setId(param.getId());
        data.setTitle(param.getTitle());
        data.setUpdateId(UserThreadUtil.getUserId());
        data.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        if (questionTargetService.updateById(data)) {
            List<Integer> keepIds = new ArrayList<>();
            param.getChildren().forEach(row -> {
                if (row.id != 0) {
                    keepIds.add(row.id);
                }
            });
            // 清理无效数据
            Wrapper<QuestionTarget> w = new EntityWrapper<>();
            w.notIn(QuestionTarget.ID, keepIds);
            w.eq(QuestionTarget.PID, data.getId());
            questionTargetService.delete(w);
            // 更新数据
            List<QuestionTargetParam> children = param.getChildren();
            for (QuestionTargetParam child : children) {
                QuestionTarget son = new QuestionTarget();
                son.setId(child.getId());
                son.setPid(data.getId());
                son.setTitle(child.getTitle());
                son.setStatus(1);
                son.setSort(100);
                son.setRemarks("");
                son.setUpdateId(UserThreadUtil.getUserId());
                son.setUpdateTime(new Timestamp(System.currentTimeMillis()));
                if (child.getId() == 0) {
                    son.setCreateId(UserThreadUtil.getUserId());
                    son.setCreateTime(new Timestamp(System.currentTimeMillis()));
                }
                questionTargetService.insertOrUpdate(son);
            }
            return data.getId();
        }
        return 0;
    }

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
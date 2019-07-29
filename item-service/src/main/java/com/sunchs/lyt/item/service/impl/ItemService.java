package com.sunchs.lyt.item.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.sunchs.lyt.db.business.entity.HospitalOffice;
import com.sunchs.lyt.db.business.entity.Item;
import com.sunchs.lyt.db.business.entity.ItemOffice;
import com.sunchs.lyt.db.business.entity.Questionnaire;
import com.sunchs.lyt.db.business.service.impl.HospitalOfficeServiceImpl;
import com.sunchs.lyt.db.business.service.impl.ItemOfficeServiceImpl;
import com.sunchs.lyt.db.business.service.impl.ItemServiceImpl;
import com.sunchs.lyt.db.business.service.impl.QuestionnaireServiceImpl;
import com.sunchs.lyt.framework.bean.PagingList;
import com.sunchs.lyt.framework.util.*;
import com.sunchs.lyt.item.bean.BindOfficeParam;
import com.sunchs.lyt.item.bean.ItemData;
import com.sunchs.lyt.item.bean.ItemParam;
import com.sunchs.lyt.item.bean.OfficeQuestionnaireParam;
import com.sunchs.lyt.item.enums.ItemStatusEnum;
import com.sunchs.lyt.item.exception.ItemException;
import com.sunchs.lyt.item.service.IItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;

@Service
public class ItemService implements IItemService {

    @Autowired
    private ItemServiceImpl itemService;

    @Autowired
    private ItemOfficeServiceImpl itemOfficeService;

    @Autowired
    private HospitalOfficeServiceImpl hospitalOfficeService;

    @Autowired
    private QuestionnaireServiceImpl questionnaireService;

    @Override
    public PagingList<ItemData> getPageList(ItemParam param) {
        List<ItemData> list = new ArrayList<>();
        Wrapper<Item> w = new EntityWrapper<>();
        w.orderBy(Item.ID, false);
        Page<Item> page = itemService.selectPage(new Page<>(param.getPageNow(), param.getPageSize()), w);
        page.getRecords().forEach(row -> list.add(getItemInfo(row)));
        return PagingUtil.getData(list, page.getSize(), param.getPageNow(), param.getPageSize());
    }

    @Override
    public int save(ItemParam param) {
        if (param.getId() == 0) {
            param.filterParam();
            Wrapper<Item> w = new EntityWrapper<>();
            w.eq(Item.NUMBER, param.getNumber());
            int count = itemService.selectCount(w);
            if (count > 0) {
                throw new ItemException("项目编号已存在");
            }
            Wrapper<Item> wTit = new EntityWrapper<>();
            wTit.eq(Item.TITLE, param.getTitle());
            int titCount = itemService.selectCount(wTit);
            if (titCount > 0) {
                throw new ItemException("项目标题已存在");
            }
        }


        Item data = new Item();
        data.setId(param.getId());
        // 修改时，不修改
        if (NumberUtil.isZero(param.getId())) {
            data.setHospitalId(param.getHospitalId());
            data.setNumber(param.getNumber());
            data.setTitle(param.getTitle());
            data.setCheckType(param.getCheckType());
            data.setIsBatch(param.getIsBatch());
            data.setStartTime(FormatUtil.dateTime(param.getStartTime()));
            data.setEndTime(FormatUtil.dateTime(param.getEndTime()));
            data.setCheckQty(param.getCheckQty());
        }
        data.setNowQty(param.getNowQty());
        if (StringUtil.isNotEmpty(param.getApproachTime())) {
            data.setApproachTime(FormatUtil.dateTime(param.getApproachTime()));
        }
        if (StringUtil.isNotEmpty(param.getDeliveryTime())) {
            data.setDeliveryTime(FormatUtil.dateTime(param.getDeliveryTime()));
        }
        if (StringUtil.isNotEmpty(param.getDataAnalysisTime())) {
            data.setDataAnalysisTime(FormatUtil.dateTime(param.getDataAnalysisTime()));
        }
        if (StringUtil.isNotEmpty(param.getReportStartTime())) {
            data.setReportStartTime(FormatUtil.dateTime(param.getReportStartTime()));
        }
        if (StringUtil.isNotEmpty(param.getReportEndTime())) {
            data.setReportEndTime(FormatUtil.dateTime(param.getReportEndTime()));
        }
        data.setUserId(param.getUserId());
        data.setUpdateId(UserThreadUtil.getUserId());
        data.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        if (NumberUtil.isZero(param.getId())) {
            data.setCreateId(UserThreadUtil.getUserId());
            data.setCreateTime(new Timestamp(System.currentTimeMillis()));
        }
        return itemService.insertOrUpdate(data) ? data.getId() : 0;
    }

    @Override
    public int bindOfficeQuestionnaire(BindOfficeParam param) {
        // 清理历史数据
        Wrapper<ItemOffice> w = new EntityWrapper<>();
        w.eq(ItemOffice.ITEM_ID, param.getItemId());
        itemOfficeService.delete(w);
        // 添加新数据
        List<OfficeQuestionnaireParam> bindList = param.getBindList();
        bindList.forEach(o -> {
            if (o.getQuestionnaireId() > 0) {
                o.getOfficeList().forEach(officeId -> {
                    ItemOffice data = new ItemOffice();
                    data.setItemId(param.getItemId());
                    data.setOfficeTypeId(o.getOfficeTypeId());
                    data.setOfficeId(officeId);
                    data.setQuestionnaireId(o.getQuestionnaireId());
                    // 附带科室详情
                    HospitalOffice hospitalOffice = getHospitalOfficeById(officeId);
                    if (Objects.nonNull(hospitalOffice)) {
                        data.setTitle(hospitalOffice.getTitle());
                        data.setQuantity(hospitalOffice.getQuantity());
                    }
                    itemOfficeService.insert(data);
                });
            }
        });
        return param.getItemId();
    }

    @Override
    public ItemData getById(int itemId) {
        Item item = itemService.selectById(itemId);
        if (Objects.isNull(item)) {
            return null;
        }
        ItemData data = ObjectUtil.copy(item, ItemData.class);
        data.setStartTimeName(FormatUtil.dateTime(data.getStartTime()));
        data.setEndTimeName(FormatUtil.dateTime(data.getEndTime()));
        data.setApproachTimeName(FormatUtil.dateTime(data.getApproachTime()));
        data.setDeliveryTimeName(FormatUtil.dateTime(data.getDeliveryTime()));
        data.setDataAnalysisTimeName(FormatUtil.dateTime(data.getDataAnalysisTime()));
        data.setReportStartTimeName(FormatUtil.dateTime(data.getReportStartTime()));
        data.setReportEndTimeName(FormatUtil.dateTime(data.getReportEndTime()));
        return data;
    }

    @Override
    public List<Map<String, Object>> getOfficeList(ItemParam param) {
        List<Map<String, Object>> list = new ArrayList<>();
        Wrapper<ItemOffice> wrapper = new EntityWrapper<ItemOffice>()
                .eq(ItemOffice.ITEM_ID, param.getId());
        itemOfficeService.selectList(wrapper).forEach(o -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", o.getId());
            map.put("officeTypeId", o.getOfficeTypeId());
            map.put("officeId", o.getOfficeId());
            map.put("questionnaireId", o.getQuestionnaireId());
            map.put("title", o.getTitle());
            map.put("quantity", o.getQuantity());
            list.add(map);
        });
        return list;
    }

    private List<Integer> getHospitalOffice(BindOfficeParam param) {
        Wrapper<Item> w = new EntityWrapper<>();
        w.eq(Item.ID, param.getItemId());
        Item item = itemService.selectOne(w);
        if (Objects.isNull(item)) {
            throw new ItemException("项目不存在，ID：" + param.getItemId());
        }

        List<Integer> ids = new ArrayList<>();

        Wrapper<HospitalOffice> hoWhere = new EntityWrapper<>();
        hoWhere.eq(HospitalOffice.HOSPITAL_ID, item.getHospitalId());
        List<HospitalOffice> officeList = hospitalOfficeService.selectList(hoWhere);
        officeList.forEach(office -> ids.add(office.getId()));
        return ids;
    }

    private ItemData getItemInfo(Item item) {
        ItemData res = ObjectUtil.copy(item, ItemData.class);

        // 获取问卷列表
        res.setQuestionnaireList(getQuestionnaireList(item));

        // 状态
        res.setStatusName(ItemStatusEnum.get(res.getStatus()));

        // 完成时间
        res.setFinishTimeName(FormatUtil.dateTime(res.getEndTime()));

        // 创建时间
        res.setCreateTimeName(FormatUtil.dateTime(res.getCreateTime()));

        return res;
    }

    private HospitalOffice getHospitalOfficeById(int officeId) {
        Wrapper<HospitalOffice> wrapper = new EntityWrapper<HospitalOffice>()
                .eq(HospitalOffice.ID, officeId);
        return hospitalOfficeService.selectOne(wrapper);
    }

    private List<Questionnaire> getQuestionnaireList(Item item) {
        Set<Integer> ids = getQuestionnaireIds(item);
        if (ids.size() == 0) {
            return new ArrayList<>();
        }
        Wrapper<Questionnaire> wrapper = new EntityWrapper<Questionnaire>()
                .in(Questionnaire.ID, ids);
        return questionnaireService.selectList(wrapper);
    }

    private Set<Integer> getQuestionnaireIds(Item item) {
        Set<Integer> ids = new HashSet<>();
        Wrapper<ItemOffice> wrapper = new EntityWrapper<ItemOffice>()
                .eq(ItemOffice.ITEM_ID, item.getId());
        itemOfficeService.selectList(wrapper).forEach(q -> {
            ids.add(q.getQuestionnaireId());
        });
        return ids;
    }
}

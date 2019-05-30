package com.sunchs.lyt.item.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.sunchs.lyt.db.business.entity.HospitalOffice;
import com.sunchs.lyt.db.business.entity.Items;
import com.sunchs.lyt.db.business.entity.ItemsOffice;
import com.sunchs.lyt.db.business.service.impl.HospitalOfficeServiceImpl;
import com.sunchs.lyt.db.business.service.impl.ItemsOfficeServiceImpl;
import com.sunchs.lyt.db.business.service.impl.ItemsServiceImpl;
import com.sunchs.lyt.framework.util.FormatUtil;
import com.sunchs.lyt.framework.util.NumberUtil;
import com.sunchs.lyt.framework.util.StringUtil;
import com.sunchs.lyt.item.bean.ItemParam;
import com.sunchs.lyt.item.bean.OfficeQuestionnaireParam;
import com.sunchs.lyt.item.exception.ItemException;
import com.sunchs.lyt.item.service.IItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class ItemService implements IItemService {

    @Autowired
    private ItemsServiceImpl itemsService;

    @Autowired
    private ItemsOfficeServiceImpl itemsOfficeService;

    @Autowired
    private HospitalOfficeServiceImpl hospitalOfficeService;

    @Override
    public int save(ItemParam param) {
        param.filterParam();

        Items data = new Items();
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
        // TODO::用户ID
        data.setUpdateId(0);
        data.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        if (NumberUtil.isZero(param.getId())) {
            // TODO::用户ID
            data.setCreateId(0);
            data.setCreateTime(new Timestamp(System.currentTimeMillis()));
        }
        return itemsService.insertOrUpdate(data) ? data.getId() : 0;
    }

    @Override
    public void bindOfficeQuestionnaire(OfficeQuestionnaireParam param) {
        param.filterParam();
        List<Integer> hospitalOfficeIds = getHospitalOffice(param);

        // 清理历史数据
        Wrapper<ItemsOffice> w = new EntityWrapper<>();
        w.eq(ItemsOffice.ITEM_ID, param.getItemId());
        itemsOfficeService.delete(w);
        // 添加新数据
        param.getOfficeIds().forEach(officeId -> {
            if (hospitalOfficeIds.contains(officeId)) {
                ItemsOffice data = new ItemsOffice();
                data.setItemId(param.getItemId());
                data.setOfficeId(officeId);
                data.setQuestionnaireId(param.getQuestionnaireId());
                itemsOfficeService.insert(data);
            }
        });
    }

    private List<Integer> getHospitalOffice(OfficeQuestionnaireParam param) {
        Wrapper<Items> w = new EntityWrapper<>();
        w.eq(Items.ID, param.getItemId());
        Items item = itemsService.selectOne(w);
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
}

package com.sunchs.lyt.item.service.impl;

import com.sunchs.lyt.db.business.entity.Items;
import com.sunchs.lyt.db.business.service.impl.ItemsServiceImpl;
import com.sunchs.lyt.framework.util.FormatUtil;
import com.sunchs.lyt.framework.util.JsonUtil;
import com.sunchs.lyt.framework.util.NumberUtil;
import com.sunchs.lyt.framework.util.StringUtil;
import com.sunchs.lyt.item.bean.ItemParam;
import com.sunchs.lyt.item.service.IItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ItemService implements IItemService {

    @Autowired
    private ItemsServiceImpl itemsService;

    @Override
    public int save(ItemParam param) {
        param.filter();

        Items data = new Items();
        data.setId(param.getId());
        data.setHospitalId(param.getHospitalId());
        // 修改时，不修改
        if (NumberUtil.isZero(param.getId())) {
            data.setNumber(param.getNumber());
        }
        data.setTitle(param.getTitle());
        data.setCheckType(param.getCheckType());
        data.setIsBatch(param.getIsBatch());
        data.setStartTime(FormatUtil.dateTime(param.getStartTime()));
        data.setEndTime(FormatUtil.dateTime(param.getEndTime()));
        data.setCheckQty(param.getCheckQty());
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
        return itemsService.insertOrUpdate(data) ? data.getId() : 0;
    }

}

package com.sunchs.lyt.item.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.sunchs.lyt.db.business.entity.ItemOffice;
import com.sunchs.lyt.db.business.service.impl.ItemOfficeServiceImpl;
import com.sunchs.lyt.framework.util.ObjectUtil;
import com.sunchs.lyt.item.bean.AnswerParam;
import com.sunchs.lyt.item.bean.ItemOfficeFooData;
import com.sunchs.lyt.item.exception.ItemException;
import com.sunchs.lyt.item.service.IAnswerFooService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class AnswerFooService implements IAnswerFooService {

    @Autowired
    private ItemOfficeServiceImpl itemOfficeService;

    @Override
    public void saveAnswer(AnswerParam param) {

    }

    @Override
    public ItemOfficeFooData getItemOfficeInfo(int itemId, int officeId) {
        ItemOffice itemOffice = getItemOffice(itemId, officeId);
        if (Objects.isNull(itemOffice)) {
            throw new ItemException("项目科室已停用！");
        }

        ItemOfficeFooData data = ObjectUtil.copy(itemOffice, ItemOfficeFooData.class);



        return data;
    }

    private ItemOffice getItemOffice(int itemId, int officeId) {
        Wrapper<ItemOffice> wrapper = new EntityWrapper<ItemOffice>()
                .eq(ItemOffice.ITEM_ID, itemId)
                .eq(ItemOffice.OFFICE_ID, officeId);
        return itemOfficeService.selectOne(wrapper);
    }
}

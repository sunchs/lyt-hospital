package com.sunchs.lyt.item.service.impl;

import com.sunchs.lyt.framework.bean.PagingList;
import com.sunchs.lyt.item.bean.AnswerParam;
import com.sunchs.lyt.item.bean.ItemData;
import com.sunchs.lyt.item.service.IAnswerService;
import org.springframework.stereotype.Service;

@Service
public class AnswerService implements IAnswerService {


    @Override
    public PagingList<ItemData> getPageList(AnswerParam param) {
        return null;
    }

    @Override
    public int save(AnswerParam param) {
        return 0;
    }
}

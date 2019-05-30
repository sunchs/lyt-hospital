package com.sunchs.lyt.item.controller;

import com.sunchs.lyt.framework.bean.RequestData;
import com.sunchs.lyt.framework.bean.ResultData;
import com.sunchs.lyt.framework.controller.BaseController;
import com.sunchs.lyt.item.bean.ItemParam;
import com.sunchs.lyt.item.bean.OfficeQuestionnaireParam;
import com.sunchs.lyt.item.service.impl.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/item")
public class ItemController extends BaseController {

    @Autowired
    private ItemService itemService;

    /**
     * 项目 添加、编辑
     */
    @PostMapping("/save")
    public ResultData save(@RequestBody RequestData data) {
        ItemParam param = data.toObject(ItemParam.class);
        return success(itemService.save(param));
    }

    /**
     * 项目 医院科室绑定问卷
     */
    @PostMapping("/bindOfficeQuestionnaire")
    public ResultData bindOfficeQuestionnaire(@RequestBody RequestData data) {
        OfficeQuestionnaireParam param = data.toObject(OfficeQuestionnaireParam.class);
        itemService.bindOfficeQuestionnaire(param);
        return success();
    }
}
package com.sunchs.lyt.item.controller;

import com.sunchs.lyt.framework.bean.RequestData;
import com.sunchs.lyt.framework.bean.ResultData;
import com.sunchs.lyt.framework.controller.BaseController;
import com.sunchs.lyt.item.bean.BindOfficeParam;
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
     * 项目分页列表
     */
    @PostMapping("/pageList")
    public ResultData getPageList(@RequestBody RequestData data) {
        ItemParam param = data.toObject(ItemParam.class);
        return success(itemService.getPageList(param));
    }

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
        BindOfficeParam param = data.toObject(BindOfficeParam.class);
        return success(itemService.bindOfficeQuestionnaire(param));
    }

    /**
     * 根据 项目ID 获取项目详情
     */
    @PostMapping("/getById")
    public ResultData getById(@RequestBody RequestData data) {
        ItemParam param = data.toObject(ItemParam.class);
        return success(itemService.getById(param.getId()));
    }

    /**
     * 获取项目所有科室
     */
    @PostMapping("/officeList")
    public ResultData getOfficeList(@RequestBody RequestData data) {
        ItemParam param = data.toObject(ItemParam.class);
        return success(itemService.getOfficeList(param));
    }
}
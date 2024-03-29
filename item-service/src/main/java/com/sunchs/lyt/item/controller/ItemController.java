package com.sunchs.lyt.item.controller;

import com.sunchs.lyt.framework.bean.RequestData;
import com.sunchs.lyt.framework.bean.ResultData;
import com.sunchs.lyt.framework.controller.BaseController;
import com.sunchs.lyt.item.bean.*;
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
     * 项目分页列表
     */
    @PostMapping("/getPageSelectList")
    public ResultData getPageSelectList(@RequestBody RequestData data) {
        ItemParam param = data.toObject(ItemParam.class);
        return success(itemService.getPageSelectList(param));
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
    @PostMapping("/officePageList")
    public ResultData getOfficePageList(@RequestBody RequestData data) {
        ItemParam param = data.toObject(ItemParam.class);
        return success(itemService.getOfficePageList(param));
    }

    /**
     * 项目科室分页
     */
    @PostMapping("/officePage")
    public ResultData getOfficeList(@RequestBody RequestData data) {
        ItemParam param = data.toObject(ItemParam.class);
        return success(itemService.getOfficeList(param));
    }

    /**
     * 修改 项目状态
     */
    @PostMapping("/updateStatus")
    public ResultData updateStatus(@RequestBody RequestData data) {
        ItemParam param = data.toObject(ItemParam.class);
        itemService.updateStatus(param);
        return success();
    }

    /**
     * 科室进度
     */
    @PostMapping("/officePlan")
    public ResultData getOfficePlan(@RequestBody RequestData data) {
        int itemId = data.getInt("id");
        int officeTypeId = data.getInt("officeTypeId");
        return success(itemService.getOfficePlan(itemId, officeTypeId));
    }

    /**
     * 更新科室抽样量
     */
    @PostMapping("/updateOfficeQuantity")
    public ResultData updateOfficeQuantity(@RequestBody RequestData data) {
        OfficeQuantityParam param = data.toObject(OfficeQuantityParam.class);
        itemService.updateOfficeQuantity(param);
        return success();
    }

    /**
     * 科室进度
     */
    @PostMapping("/unbindItemOffice")
    public ResultData unbindItemOffice(@RequestBody RequestData data) {
        int id = data.getInt("id");
        itemService.unbindItemOffice(id);
        return success();
    }

    /**
     * 科室进度
     */
    @PostMapping("/updateItemOfficeQuantity")
    public ResultData updateItemOfficeQuantity(@RequestBody RequestData data) {
        int id = data.getInt("id");
        int quantity = data.getInt("quantity");
        itemService.updateItemOfficeQuantity(id, quantity);
        return success();
    }

    @PostMapping("/addItemUser")
    public ResultData addItemUser(@RequestBody RequestData data) {
        int itemId = data.getInt("itemId");
        int userId = data.getInt("userId");
        itemService.addItemUser(itemId, userId);
        return success();
    }

    @PostMapping("/removeItemUser")
    public ResultData removeItemUser(@RequestBody RequestData data) {
        int itemId = data.getInt("itemId");
        int userId = data.getInt("userId");
        itemService.removeItemUser(itemId, userId);
        return success();
    }

    @PostMapping("/itemUserList")
    public ResultData itemUserList(@RequestBody RequestData data) {
        ItemParam param = data.toObject(ItemParam.class);
        return success(itemService.itemUserList(param));
    }

    /**
     * 同步答卷
     */
    @PostMapping("/syncAnswer")
    public ResultData syncAnswer(@RequestBody RequestData data) {
        SyncAnswerParam param = data.toObject(SyncAnswerParam.class);
        itemService.syncAnswer(param);
        return success();
    }

    @PostMapping("/userItemOfficeList")
    public ResultData userItemOfficeList(@RequestBody RequestData data) {
        return success(itemService.getUserItemOfficeList());
    }

    @PostMapping("/itemOfficeGroup")
    public ResultData getItemOfficeGroup(@RequestBody RequestData data) {
        ItemParam param = data.toObject(ItemParam.class);
        return success(itemService.getItemOfficeGroup(param));
    }

    @PostMapping("/getItemOfficeRequestQuantity")
    public ResultData getItemOfficeRequestQuantity(@RequestBody RequestData data) {
        Integer itemId = data.getInt("itemId");
        return success(itemService.getItemOfficeRequestQuantity(itemId));
    }
}
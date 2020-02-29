package com.sunchs.lyt.item.controller;

import com.sunchs.lyt.framework.bean.RequestData;
import com.sunchs.lyt.framework.bean.ResultData;
import com.sunchs.lyt.framework.controller.BaseController;
import com.sunchs.lyt.item.service.impl.ItemFileService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/itemFile")
public class ItemFileController extends BaseController {

    private ItemFileService itemFileService;

    /**
     * 根据已有项目，获取 导入答卷 的模版文件
     */
    @PostMapping("/getItemAnswerInputTemplate")
    public ResultData getItemAnswerInputTemplate(@RequestBody RequestData data) {
        Integer itemId = data.getInt("itemId");
        Integer officeType = data.getInt("officeType");
        Integer officeId = data.getInt("officeId");
        String path = itemFileService.getItemAnswerInputTemplate(itemId, officeType, officeId);
        return success("http://47.107.255.115:8008/output/download/?fileName="+path);
    }

}
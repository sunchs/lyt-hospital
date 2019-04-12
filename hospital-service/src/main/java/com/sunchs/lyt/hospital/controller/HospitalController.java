package com.sunchs.lyt.hospital.controller;

import com.sunchs.lyt.framework.bean.RequestData;
import com.sunchs.lyt.framework.bean.ResultData;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/hospital")
public interface HospitalController
{
    /**
     * 医院信息 添加、编辑
     */
    @PostMapping("/save")
    ResultData save(@RequestBody RequestData data);

    /**
     * 医院信息 删除
     */
    @PostMapping("/remove")
    ResultData remove(@RequestBody RequestData data);

    /**
     * 医院信息分页列表
     */
    @PostMapping("/pageList")
    ResultData getPageList(@RequestBody RequestData data);

    /**
     * 根据 医院ID 获取医院详情
     */
    @PostMapping("/getById")
    ResultData getById(@RequestBody RequestData data);
}
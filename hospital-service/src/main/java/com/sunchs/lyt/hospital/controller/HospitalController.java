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
}
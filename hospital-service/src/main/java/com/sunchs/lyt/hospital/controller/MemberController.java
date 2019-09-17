package com.sunchs.lyt.hospital.controller;

import com.sunchs.lyt.db.business.entity.Member;
import com.sunchs.lyt.framework.bean.RequestData;
import com.sunchs.lyt.framework.bean.ResultData;
import com.sunchs.lyt.framework.controller.BaseController;
import com.sunchs.lyt.hospital.bean.MemberParam;
import com.sunchs.lyt.hospital.exception.HospitalException;
import com.sunchs.lyt.hospital.service.impl.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequestMapping("/member")
public class MemberController extends BaseController {

    @Autowired
    private MemberService memberService;


    @PostMapping("/save")
    public ResultData save(@RequestBody RequestData data) {
        MemberParam param = data.toObject(MemberParam.class);


        return success();
    }

    @PostMapping("/getCode")
    public ResultData getCode(@RequestBody RequestData data) {
        MemberParam param = data.toObject(MemberParam.class);
        return success(memberService.setCodeLog(param));
    }

    @PostMapping("/getMember")
    public ResultData getMember(@RequestBody RequestData data) {
        MemberParam param = data.toObject(MemberParam.class);
        Member member = memberService.getMember(param);
        if (Objects.isNull(member)) {
            throw new HospitalException("无成员信息");
        }
        return success(member);
    }
}
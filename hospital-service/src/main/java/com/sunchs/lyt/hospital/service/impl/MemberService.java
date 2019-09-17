package com.sunchs.lyt.hospital.service.impl;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.sunchs.lyt.db.business.entity.Member;
import com.sunchs.lyt.db.business.service.impl.MemberServiceImpl;
import com.sunchs.lyt.framework.constants.CacheKeys;
import com.sunchs.lyt.framework.constants.DateTimes;
import com.sunchs.lyt.framework.util.RedisUtil;
import com.sunchs.lyt.framework.util.StringUtil;
import com.sunchs.lyt.hospital.bean.MemberParam;
import com.sunchs.lyt.hospital.exception.HospitalException;
import com.sunchs.lyt.hospital.service.IMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class MemberService implements IMemberService {

    @Autowired
    private MemberServiceImpl memberService;

    @Override
    public int setCodeLog(MemberParam param) {
        checkPhone(param);
        // 保存验证码
        int random = (int) (Math.random()*(9999-1111)+1111);
        RedisUtil.setValue(CacheKeys.MEMBER_PHONE_CODE + param.getPhone(), String.valueOf(random), DateTimes.MINUTES * 5);

        String msg = "【砺扬泰医管】您正在登录员工问卷，验证码"+random+"，请在5分钟内登录。请勿将验证码泄露于他人。";
        String urlStr = "http://118.126.4.52:7891/mt?un=300377&pw=LYT300377&da="+param.getPhone()+"&sm="+msg+"&dc=15&rd=1&rf=2&tf=3";

        HttpURLConnection connection = null;
        try {
            URL url = new URL(urlStr);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(15000);
            connection.setReadTimeout(60000);
            connection.connect();
            if (connection.getResponseCode() == 200) {
                return random;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            connection.disconnect();// 关闭远程连接
        }
        return 0;
    }

    @Override
    public Member getMember(MemberParam param) {
        checkPhone(param);
        // 查询
        Wrapper<Member> wrapper = new EntityWrapper<Member>()
                .eq(Member.PHONE, param.getPhone());
        return memberService.selectOne(wrapper);
    }

    @Override
    public int save(MemberParam param) {
        Member member = getMember(param);

        String code = RedisUtil.getValue(CacheKeys.MEMBER_PHONE_CODE + param.getPhone());
        if ( ! code.equals(param.getAuthCode())) {
            throw new HospitalException("验证码不正确！");
        }

        Member data = new Member();
        if (Objects.nonNull(member)) {
            data.setId(member.getId());
        }
        data.setPhone(param.getPhone());
        data.setName(param.getName());
        data.setIdentityCard(param.getIdentityCard());
        if (Objects.isNull(member)) {
            data.setCreateTime(new Timestamp(System.currentTimeMillis()));
        }
        memberService.insertOrUpdate(data);
        return data.getId();
    }

    private void checkPhone(MemberParam param) {
        if (StringUtil.isEmpty(param.getPhone())) {
            throw new HospitalException("手机号码不能为空！");
        } else if (param.getPhone().length() != 11) {
            throw new HospitalException("手机号应为11位数！");
        } else {
            String regex = "^((13[0-9])|(14[5,7,9])|(15([0-3]|[5-9]))|(166)|(17[0,1,3,5,6,7,8])|(18[0-9])|(19[8|9]))\\d{8}$";
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(param.getPhone());
            boolean isMatch = m.matches();
            if ( ! isMatch) {
                throw new HospitalException("请填入正确的手机号！");
            }
        }
    }
}

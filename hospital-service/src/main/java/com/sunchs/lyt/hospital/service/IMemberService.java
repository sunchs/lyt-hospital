package com.sunchs.lyt.hospital.service;

import com.sunchs.lyt.db.business.entity.Member;
import com.sunchs.lyt.hospital.bean.MemberParam;

public interface IMemberService {

    Member getMember(MemberParam param);

    int setCodeLog(MemberParam param);
}

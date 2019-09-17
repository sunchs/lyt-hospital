package com.sunchs.lyt.db.business.service.impl;

import com.sunchs.lyt.db.business.entity.Member;
import com.sunchs.lyt.db.business.mapper.MemberMapper;
import com.sunchs.lyt.db.business.service.IMemberService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 成员信息表 服务实现类
 * </p>
 *
 * @author king
 * @since 2019-09-17
 */
@Service
public class MemberServiceImpl extends ServiceImpl<MemberMapper, Member> implements IMemberService {

}

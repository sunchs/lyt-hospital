package com.sunchs.lyt.db.business.service.impl;

import com.sunchs.lyt.db.business.entity.User;
import com.sunchs.lyt.db.business.mapper.UserMapper;
import com.sunchs.lyt.db.business.service.IUserService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author king
 * @since 2019-06-30
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

}

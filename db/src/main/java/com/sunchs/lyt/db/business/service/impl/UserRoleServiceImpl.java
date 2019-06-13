package com.sunchs.lyt.db.business.service.impl;

import com.sunchs.lyt.db.business.entity.UserRole;
import com.sunchs.lyt.db.business.mapper.UserRoleMapper;
import com.sunchs.lyt.db.business.service.IUserRoleService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户 - 角色 关系表 服务实现类
 * </p>
 *
 * @author king
 * @since 2019-06-11
 */
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements IUserRoleService {

}

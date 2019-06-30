package com.sunchs.lyt.db.business.service.impl;

import com.sunchs.lyt.db.business.entity.UserHospital;
import com.sunchs.lyt.db.business.mapper.UserHospitalMapper;
import com.sunchs.lyt.db.business.service.IUserHospitalService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户 - 医院 关系表 服务实现类
 * </p>
 *
 * @author king
 * @since 2019-07-01
 */
@Service
public class UserHospitalServiceImpl extends ServiceImpl<UserHospitalMapper, UserHospital> implements IUserHospitalService {

}

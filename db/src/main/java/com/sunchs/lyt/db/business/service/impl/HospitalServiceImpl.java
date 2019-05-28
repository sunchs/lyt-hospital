package com.sunchs.lyt.db.business.service.impl;

import com.sunchs.lyt.db.business.entity.Hospital;
import com.sunchs.lyt.db.business.mapper.HospitalMapper;
import com.sunchs.lyt.db.business.service.IHospitalService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 医院信息表 服务实现类
 * </p>
 *
 * @author king
 * @since 2019-05-28
 */
@Service
public class HospitalServiceImpl extends ServiceImpl<HospitalMapper, Hospital> implements IHospitalService {

}

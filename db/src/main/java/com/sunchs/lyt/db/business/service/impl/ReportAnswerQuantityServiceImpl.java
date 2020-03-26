package com.sunchs.lyt.db.business.service.impl;

import com.sunchs.lyt.db.business.entity.ReportAnswerQuantity;
import com.sunchs.lyt.db.business.mapper.ReportAnswerQuantityMapper;
import com.sunchs.lyt.db.business.service.IReportAnswerQuantityService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 答卷数量表 服务实现类
 * </p>
 *
 * @author king
 * @since 2019-11-08
 */
@Service
public class ReportAnswerQuantityServiceImpl extends ServiceImpl<ReportAnswerQuantityMapper, ReportAnswerQuantity> implements IReportAnswerQuantityService {
    @Override
    public List<ReportAnswerQuantity> getTargetSatisfyList(Integer itemId, Integer targetId) {
        return baseMapper.getTargetSatisfyList(itemId, targetId);
    }
}

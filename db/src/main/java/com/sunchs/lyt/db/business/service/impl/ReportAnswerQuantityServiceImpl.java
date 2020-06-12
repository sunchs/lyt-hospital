package com.sunchs.lyt.db.business.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.sunchs.lyt.db.bean.AnswerQuantityParam;
import com.sunchs.lyt.db.business.entity.ReportAnswerQuantity;
import com.sunchs.lyt.db.business.mapper.ReportAnswerQuantityMapper;
import com.sunchs.lyt.db.business.service.IReportAnswerQuantityService;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

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
    public List<ReportAnswerQuantity> getTargetSatisfyThreeList(Integer itemId, Integer targetId) {
        return baseMapper.getTargetSatisfyThreeList(itemId, targetId);
    }

    @Override
    public List<ReportAnswerQuantity> getTargetSatisfyTwoList(Integer itemId, Integer targetId) {
        return baseMapper.getTargetSatisfyTwoList(itemId, targetId);
    }

    @Override
    public List<ReportAnswerQuantity> getTargetSatisfyList(Integer itemId, Integer officeType, List<Integer> targetIds) {
        return baseMapper.getTargetSatisfyList(itemId, officeType, targetIds);
    }

    @Override
    public List<ReportAnswerQuantity> getItemOfficeTargetSatisfyList(Integer itemId, Integer officeType, Integer officeId, List<Integer> targetIds) {
        return baseMapper.getItemOfficeTargetSatisfyList(itemId, officeType, officeId, targetIds);
    }

    @Override
    public List<ReportAnswerQuantity> getItemOfficeTargetSatisfyListV2(AnswerQuantityParam param) {
        return baseMapper.getItemOfficeTargetSatisfyListV2(param);
    }

    @Override
    public ReportAnswerQuantity getItemOfficeSatisfyInfo(Integer itemId, Integer officeType, Integer officeId, List<Integer> targetIds) {
        return baseMapper.getItemOfficeSatisfyInfo(itemId, officeType, officeId, targetIds);
    }

    @Override
    public List<ReportAnswerQuantity> getItemOfficeSatisfyQuestionList(Integer itemId, Integer officeType, Integer officeId, List<Integer> targetIds) {
        return baseMapper.getItemOfficeSatisfyQuestionList(itemId, officeType, officeId, targetIds);
    }

    @Override
    public List<ReportAnswerQuantity> getItemAllOfficeSatisfyList(Integer itemId, Set<Integer> officeTypeIds, Set<Integer> officeIds) {
        return baseMapper.getItemAllOfficeSatisfyList(itemId, officeTypeIds, officeIds);
    }

    @Override
    public List<ReportAnswerQuantity> getCustomOfficeTargetSatisfyList(List<Integer> answerIds, List<Integer> targetIds) {
        return baseMapper.getCustomOfficeTargetSatisfyList(answerIds, targetIds);
    }
}

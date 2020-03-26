package com.sunchs.lyt.db.business.service;

import com.sunchs.lyt.db.business.entity.ReportAnswerQuantity;
import com.baomidou.mybatisplus.service.IService;

import java.util.List;

/**
 * <p>
 * 答卷数量表 服务类
 * </p>
 *
 * @author king
 * @since 2019-11-08
 */
public interface IReportAnswerQuantityService extends IService<ReportAnswerQuantity> {

    /**
     * 获取三级指标满意度列表
     */
    List<ReportAnswerQuantity> getTargetSatisfyThreeList(Integer itemId, Integer targetId);

    /**
     * 获取二级指标满意度列表
     */
    List<ReportAnswerQuantity> getTargetSatisfyTwoList(Integer itemId, Integer targetId);

    /**
     * 根据ID集合 获取三级指标满意度列表
     */
    List<ReportAnswerQuantity> getTargetSatisfyList(List<Integer> targetIds);
}

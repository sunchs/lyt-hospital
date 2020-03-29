package com.sunchs.lyt.db.business.service;

import com.sunchs.lyt.db.business.entity.ReportAnswerQuantity;
import com.baomidou.mybatisplus.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

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
    List<ReportAnswerQuantity> getTargetSatisfyList(Integer itemId, Integer officeType, List<Integer> targetIds);

    /**
     * 根据 科室ID、指标ID集合 获取三级指标满意度列表
     */
    List<ReportAnswerQuantity> getItemOfficeTargetSatisfyList(Integer itemId, Integer officeType, Integer officeId, List<Integer> targetIds);

    /**
     * 根据 科室ID、指标ID集合 获取三级指标满意度列表
     */
    ReportAnswerQuantity getItemOfficeSatisfyInfo(Integer itemId, Integer officeType, Integer officeId, List<Integer> targetIds);

    /**
     * 获取单科室相关数据
     */
    List<ReportAnswerQuantity> getItemOfficeSatisfyQuestionList(Integer itemId, Integer officeType, Integer officeId, List<Integer> targetIds);

    /**
     * 获取全院的统计数据
     */
    List<ReportAnswerQuantity> getItemAllOfficeSatisfyList(Integer itemId, Set<Integer> officeTypeIds, Set<Integer> officeIds);

}

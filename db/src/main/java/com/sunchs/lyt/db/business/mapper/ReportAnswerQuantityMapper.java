package com.sunchs.lyt.db.business.mapper;

import com.sunchs.lyt.db.business.entity.ReportAnswerQuantity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 答卷数量表 Mapper 接口
 * </p>
 *
 * @author king
 * @since 2019-11-08
 */
public interface ReportAnswerQuantityMapper extends BaseMapper<ReportAnswerQuantity> {

    List<ReportAnswerQuantity> getTargetSatisfyThreeList(@Param("itemId") Integer itemId, @Param("targetId") Integer targetId);

    List<ReportAnswerQuantity> getTargetSatisfyTwoList(@Param("itemId") Integer itemId, @Param("targetId") Integer targetId);

    List<ReportAnswerQuantity> getTargetSatisfyList(@Param("itemId") Integer itemId, @Param("officeType") Integer officeType, @Param("targetIds") List<Integer> targetIds);
}

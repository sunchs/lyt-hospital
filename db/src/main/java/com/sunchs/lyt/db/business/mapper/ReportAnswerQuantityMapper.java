package com.sunchs.lyt.db.business.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.sunchs.lyt.db.business.entity.ReportAnswerQuantity;
import com.sunchs.lyt.db.bean.AnswerQuantityParam;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

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

    List<ReportAnswerQuantity> getItemOfficeTargetSatisfyList(@Param("itemId") Integer itemId, @Param("officeType") Integer officeType, @Param("officeId") Integer officeId, @Param("targetIds") List<Integer> targetIds);

    List<ReportAnswerQuantity> getItemOfficeTargetSatisfyListV2(@Param("param") AnswerQuantityParam param);

    ReportAnswerQuantity getItemOfficeSatisfyInfo(@Param("itemId") Integer itemId, @Param("officeType") Integer officeType, @Param("officeId") Integer officeId, @Param("targetIds") List<Integer> targetIds);

    List<ReportAnswerQuantity> getItemOfficeSatisfyQuestionList(@Param("itemId") Integer itemId, @Param("officeType") Integer officeType, @Param("officeId") Integer officeId, @Param("targetIds") List<Integer> targetIds);

    List<ReportAnswerQuantity> getItemAllOfficeSatisfyList(@Param("itemId") Integer itemId, @Param("officeTypeIds") Set<Integer> officeTypeIds, @Param("officeIds") Set<Integer> officeIds);
}

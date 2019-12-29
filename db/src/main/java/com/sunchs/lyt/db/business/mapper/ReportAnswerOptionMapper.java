package com.sunchs.lyt.db.business.mapper;

import com.sunchs.lyt.db.business.entity.ReportAnswerOption;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.sunchs.lyt.db.business.entity.ReportAnswerQuantity;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 答案详情表 Mapper 接口
 * </p>
 *
 * @author king
 * @since 2019-12-30
 */
public interface ReportAnswerOptionMapper extends BaseMapper<ReportAnswerOption> {

    @Select("SELECT question_id as questionId,question_name as questionName,option_id as optionId,option_name as optionName,COUNt(1) as quantity " +
            "FROM report_answer_option WHERE answer_id IN(${answerIds}) GROUP BY question_id,option_id")
    List<ReportAnswerQuantity> getReportAnswer(@Param("answerIds") String answerIds);
}

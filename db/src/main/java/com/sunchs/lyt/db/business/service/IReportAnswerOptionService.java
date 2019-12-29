package com.sunchs.lyt.db.business.service;

import com.sunchs.lyt.db.business.entity.ReportAnswerOption;
import com.baomidou.mybatisplus.service.IService;
import com.sunchs.lyt.db.business.entity.ReportAnswerQuantity;

import java.util.List;

/**
 * <p>
 * 答案详情表 服务类
 * </p>
 *
 * @author king
 * @since 2019-12-30
 */
public interface IReportAnswerOptionService extends IService<ReportAnswerOption> {
    List<ReportAnswerQuantity> getReportAnswerData(List<Long> answerIds);
}

package com.sunchs.lyt.db.business.service.impl;

import com.sunchs.lyt.db.business.entity.ReportAnswerOption;
import com.sunchs.lyt.db.business.entity.ReportAnswerQuantity;
import com.sunchs.lyt.db.business.mapper.ReportAnswerOptionMapper;
import com.sunchs.lyt.db.business.service.IReportAnswerOptionService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 答案详情表 服务实现类
 * </p>
 *
 * @author king
 * @since 2019-08-14
 */
@Service
public class ReportAnswerOptionServiceImpl extends ServiceImpl<ReportAnswerOptionMapper, ReportAnswerOption> implements IReportAnswerOptionService {

    @Override
    public List<ReportAnswerQuantity> getReportAnswerData(List<Long> answerIds) {
        List<String> ids = new ArrayList<>();
        answerIds.forEach(id->ids.add(String.valueOf(id)));
        String join = String.join(",", ids);
        return baseMapper.getReportAnswer(join);
    }

}

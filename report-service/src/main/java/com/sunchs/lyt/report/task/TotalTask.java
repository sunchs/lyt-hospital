package com.sunchs.lyt.report.task;

import com.sunchs.lyt.report.service.impl.ReportFactoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
public class TotalTask {

    @Autowired
    private ReportFactoryService reportFactoryService;

    /**
     * 统计答卷数量 - 定时器
     */
    @Scheduled(cron = "0 0 3 * * ?")
    public void totalReportAnswerQuantity() {
        reportFactoryService.makeAnswerQuantity();
        reportFactoryService.makeAnswerSatisfy();
    }
}

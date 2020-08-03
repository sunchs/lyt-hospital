package com.sunchs.lyt.report.service;

public interface IReportFactoryService {

    /**
     * 生成答题数量
     */
    void makeAnswerQuantity(Integer itemId);

    /**
     * 生成答题满意度
     */
    void makeAnswerSatisfy();
}

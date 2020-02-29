package com.sunchs.lyt.item.service;

public interface IItemFileService {

    /**
     * 获取 导入模版 文件
     */
    String getItemAnswerInputTemplate(Integer itemId, Integer officeType, Integer officeId);

}

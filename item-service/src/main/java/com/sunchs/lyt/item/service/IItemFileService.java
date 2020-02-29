package com.sunchs.lyt.item.service;

import org.springframework.web.multipart.MultipartFile;

public interface IItemFileService {

    /**
     * 获取 导入模版 文件
     */
    String getItemAnswerInputTemplate(Integer itemId, Integer officeType, Integer officeId);

    /**
     * 导入数据
     */
    String inputItemAnswer();

    /**
     * 上传文件
     */
    String uploadFile(MultipartFile file);
}

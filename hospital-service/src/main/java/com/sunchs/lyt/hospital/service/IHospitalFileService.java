package com.sunchs.lyt.hospital.service;

import org.springframework.web.multipart.MultipartFile;

public interface IHospitalFileService {

    /**
     * 上传文件
     */
    String uploadFile(MultipartFile file);
}

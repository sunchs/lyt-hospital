package com.sunchs.lyt.hospital.service.impl;

import com.sunchs.lyt.hospital.exception.HospitalException;
import com.sunchs.lyt.hospital.service.IHospitalFileService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class HospitalFileService implements IHospitalFileService {
    @Override
    public String uploadFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new HospitalException("请选择导入文件");
        }
        String rootPath = "/lyt";
        String fileName = "/complaint";
        try {
            File root = new File(rootPath);
            if ( ! root.exists()) {
                root.mkdirs();
            }
            File dir = new File(fileName);
            if ( ! dir.exists()) {
                dir.mkdirs();
            }
            // 扩展名
            String originalFilename = file.getOriginalFilename();
            String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
            // 用日期拼接文件名
            DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
            String dateString = dateFormat.format(new Date());
            fileName += "/" + dateString + "-" + System.currentTimeMillis() + suffix;
            System.out.println(fileName);

            File fileUpload = new File(fileName);
            file.transferTo(fileUpload);
        } catch (IOException e) {
            throw new HospitalException("上传文件到服务器失败：" + e.toString());
        }

        return fileName;
    }
}

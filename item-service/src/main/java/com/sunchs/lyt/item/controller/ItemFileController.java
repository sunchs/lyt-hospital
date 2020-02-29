package com.sunchs.lyt.item.controller;

import com.sunchs.lyt.framework.bean.RequestData;
import com.sunchs.lyt.framework.bean.ResultData;
import com.sunchs.lyt.framework.controller.BaseController;
import com.sunchs.lyt.item.service.impl.ItemFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;

@RestController
@RequestMapping("/itemFile")
public class ItemFileController extends BaseController {

    @Autowired
    private ItemFileService itemFileService;

    /**
     * 根据已有项目，获取 导入答卷 的模版文件
     */
    @PostMapping("/getItemAnswerInputTemplate")
    public ResultData getItemAnswerInputTemplate(@RequestBody RequestData data) {
        Integer itemId = data.getInt("itemId");
        Integer officeType = data.getInt("officeType");
        Integer officeId = data.getInt("officeId");
        String path = itemFileService.getItemAnswerInputTemplate(itemId, officeType, officeId);
        return success("http://47.107.255.115:8004/itemFile/download/?fileName="+path);
    }

    @GetMapping("/download")
    public String downloadFile(@RequestParam("fileName") String fileName, HttpServletResponse response ) {
        // 获取指定目录下的第一个文件
        File file = new File(fileName);
        System.out.println(fileName);

        // 如果文件名存在，则进行下载
        if (file.exists()) {
            // 配置文件下载
            response.setHeader("content-type", "application/octet-stream");
            response.setContentType("application/octet-stream");
            // 下载文件能正常显示中文
            try {
                response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            // 实现文件下载
            byte[] buffer = new byte[1024];
            FileInputStream fis = null;
            BufferedInputStream bis = null;
            try {
                fis = new FileInputStream(file);
                bis = new BufferedInputStream(fis);
                OutputStream os = response.getOutputStream();
                int i = bis.read(buffer);
                while (i != -1) {
                    os.write(buffer, 0, i);
                    i = bis.read(buffer);
                }
                System.out.println("Download the song successfully!");
            }
            catch (Exception e) {
                System.out.println("Download the song failed!");
            }
            finally {
                if (bis != null) {
                    try {
                        bis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return null;
    }
}
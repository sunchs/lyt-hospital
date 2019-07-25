package com.sunchs.lyt.question.controller;

import com.sunchs.lyt.framework.bean.RequestData;
import com.sunchs.lyt.framework.bean.ResultData;
import com.sunchs.lyt.framework.controller.BaseController;
import com.sunchs.lyt.question.bean.QuestionnaireParam;
import com.sunchs.lyt.question.service.impl.QuestionnaireService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;

@RestController
@RequestMapping("/questionnaire")
public class QuestionnaireController extends BaseController {

    @Autowired
    QuestionnaireService questionnaireService;

    /**
     * 问卷 添加、编辑
     */
    @PostMapping("/getById")
    public ResultData getById(@RequestBody RequestData data) {
        QuestionnaireParam param = data.toObject(QuestionnaireParam.class);
        return success(questionnaireService.getById(param.getId()));
    }

    /**
     * 问卷 添加、编辑
     */
    @PostMapping("/save")
    public ResultData save(@RequestBody RequestData data) {
        QuestionnaireParam param = data.toObject(QuestionnaireParam.class);
        return success(questionnaireService.save(param));
    }

    /**
     * 问卷 添加、编辑
     */
    @PostMapping("/updateStatus")
    public ResultData updateStatus(@RequestBody RequestData data) {
        QuestionnaireParam param = data.toObject(QuestionnaireParam.class);
        questionnaireService.updateStatus(param);
        return success();
    }

    /**
     * 问卷列表
     */
    @PostMapping("/pageList")
    public ResultData getPageList(@RequestBody RequestData data) {
        QuestionnaireParam param = data.toObject(QuestionnaireParam.class);
        return success(questionnaireService.getPageList(param));
    }

    /**
     * 获取可用问卷
     */
    @PostMapping("/usableList")
    public ResultData usableList(@RequestBody RequestData data) {
        QuestionnaireParam param = data.toObject(QuestionnaireParam.class);
        return success(questionnaireService.getUsableList(param.getHospitalId()));
    }


    /**
     * 导出文件
     */
    @PostMapping("/outputFile")
    public ResultData outputFile(@RequestBody RequestData data) {
        QuestionnaireParam param = data.toObject(QuestionnaireParam.class);
        String path = questionnaireService.createExcelFile(param.getId());
        return success("http://47.107.255.115:8002/questionnaire/download/?fileName="+path);
    }

    //实现Spring Boot 的文件下载功能，映射网址为/download
    @GetMapping("/download")
    public String downloadFile(@RequestParam("fileName") String fileName, HttpServletResponse response ) {

        // 获取指定目录下的第一个文件

            File file = new File(fileName);
        System.out.println(fileName);

            // 如果文件名存在，则进行下载
            if (file.exists()) {

//                HttpServletRequest request, HttpServletResponse response

//                         throws UnsupportedEncodingException
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

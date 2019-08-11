package com.sunchs.lyt.question.controller;

import com.sunchs.lyt.framework.bean.RequestData;
import com.sunchs.lyt.framework.bean.ResultData;
import com.sunchs.lyt.framework.controller.BaseController;
import com.sunchs.lyt.question.bean.QuestionnaireDownloadParam;
import com.sunchs.lyt.question.bean.QuestionnaireParam;
import com.sunchs.lyt.question.service.impl.QuestionnaireService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.List;

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
     * 问卷列表
     */
    @PostMapping("/download")
    public ResultData download(@RequestBody RequestData data) {
        QuestionnaireDownloadParam param = data.toObject(QuestionnaireDownloadParam.class);
        return success(questionnaireService.getDownloadList(param));
    }


    /**
     * 获取可用问卷
     */
    @PostMapping("/usableList")
    public ResultData usableList(@RequestBody RequestData data) {
        QuestionnaireParam param = data.toObject(QuestionnaireParam.class);
        return success(questionnaireService.getUsableList(param));
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

//
//class aaaa
//{
//    int status;// 状态：1成功、0失败、2，验证失效
//    String msg;// 提示信息
//    List data : [
//        {
//            int id;// 问卷ID
//            String title;// 问卷标题
//            int status;// 问卷状态，状态为1时，才可以答卷
//            int statusName; //状态名称
//            String targetOneName;// 指标名称
//            String updateTimeName;// 问卷最后更新时间，格式：2019-08-12 01:10:18
//            List questionList : [// 题目集合
//                {
//                    int id;// 题目ID
//                    String number;// 问题编号
//                    String title;// 问题内容
//                    int status;// 问题状态，1为有效问题
//                    String statusName;// 状态名称
//                    int targetOne;// 一级指标ID
//                    String targetOneName;// 一级指标名称
//                    int targetTwo;// 二级指标ID
//                    String targetTwoName;// 二级指标名称
//                    int targetThree;// 三级指标ID
//                    String targetThreeName;// 三级指标名称
//                    String optionMode;// 选项类型，radio单选，checkbox多选，text填空
//                    String optionTypeName;// 选项类型名称
//                    String updateTimeName;// 题目最后更新时间，格式：2019-08-09 16:44:30
//                    int skipMode;// 跳题模式，1为整题跳转，2为选项跳转
//                    int skipQuestionId;// 下一题ID，注：整题跳转时生效
//                    List optionList : [// 选项集合
//                        {
//                            int optionId;// 选项ID
//                            String optionName;// 选项名称
//                            int skipQuestionId;// 下一题ID，注：选项跳转时生效
//                        }
//                    ];
//                    List tagList : [// 题目标签
//                        {
//                            int type;// 标签类型ID
//                            String typeName;// 标签类型名称
//                            int tagId;// 标签ID
//                            String tagName;// 标签名称
//                        }
//                    ];
//                    int sort;// 题目排序，无跳转时，从小到大答题
//                }
//            ]
//        }
//    ]
//}

//class aa
//{
//    int itemId;// 项目ID
//    int officeId;// 科室ID
//    String patientNumber;// 患者编号
//    String startTime;// 答卷开始时间，格式：2019-08-10 00:00:00
//    String endTime;// 答卷结束时间，格式： 2019-08-10 01:00:00
//    int timeDuration;// 答卷持续时间，单位：秒，格式：3600
//    List imageList : [
//        {
//            String data;// 图片资源，格式：base64加密字符串，注：图片大小 < 80K
//        }
//    ];
//    List questionList : [
//        {
//            int questionId;// 已答的题目ID
//            String optionMode;// 选项类型，radio单选，checkbox多选，text填空
//            int optionId;// 已答选项ID，单选时必传
//            List optionIds;// 已答选项集合，多选时必传
//            String optionValue;// 已答选项值，填空时必传
//            String startTime;// 选项开始时间，格式：2019-08-10 00:00:00
//            String endTime;// 选项结束时间，格式： 2019-08-10 00:00:05
//            int timeDuration;// 选项持续时间，单位：秒，格式：5
//        }
//    ];
//}
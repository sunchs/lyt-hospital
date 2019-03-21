package com.sunchs.lyt.question.controller.impl;

import com.sunchs.lyt.framework.bean.RequestData;
import com.sunchs.lyt.framework.bean.ResultData;
import com.sunchs.lyt.framework.controller.BaseController;
import com.sunchs.lyt.question.bean.AttributeParam;
import com.sunchs.lyt.question.bean.OptionParam;
import com.sunchs.lyt.question.bean.QuestionParam;
import com.sunchs.lyt.question.bean.TargetParam;
import com.sunchs.lyt.question.controller.QuestionController;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class QuestionControllerImpl extends BaseController implements QuestionController {

    @Override
    public ResultData save(RequestData data) {
        QuestionParam param = data.toObject(QuestionParam.class);
        QuestionParam param1 = new QuestionParam();
        List<AttributeParam> attr  = new ArrayList<>();
        attr.add(new AttributeParam());
        param1.attribute = attr;
        List<OptionParam> option  = new ArrayList<>();
        option.add(new OptionParam());
        param1.option = option;
        param1.target = new TargetParam();
        return success(param1);
    }
}


//class a
//{
//    int title;// 题目
//    Integer score;// 分值
//    String remark;// 备注
//    Object target : {// 指标
//        int one;// 一级指标
//        int two;// 二级指标
//        int three;// 三级指标
//    },
//    List attribute : [// 自定义属性集合
//        {
//            int type;// 属性类型
//            int attrId;// 属性ID
//        }
//    ],
//    int optionType;// 选项类型
//    List option : [// 选项集合
//        {
//            int optionId;// 选项ID
//            String optionName;// 选项内容
//            int optionScore;// 选项分值
//            int sort;// 排序
//        }
//    ]
//}
package com.sunchs.lyt.item.bean;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.sunchs.lyt.db.business.entity.Questionnaire;
import com.sunchs.lyt.db.business.service.impl.QuestionnaireServiceImpl;
import com.sunchs.lyt.framework.util.NumberUtil;
import com.sunchs.lyt.item.exception.ItemException;
import com.sunchs.lyt.item.util.SpringContext;

import java.util.List;

public class OfficeQuestionnaireParam {

    /**
     * 科室类型
     */
    private int officeTypeId;

    /**
     * 组名
     */
    private String groupName;

    /**
     * 科室ID
     */
    private List<Integer> officeList;

    /**
     * 问卷ID
     */
    private int questionnaireId;

//    /**
//     * 问卷ID
//     */
//    private int questionnaireId;
//
//    /**
//     * 过滤参数
//     */
//    public void filterParam() {
//        if (NumberUtil.isZero(itemId)) {
//            throw new ItemException("项目ID不能为空");
//        }
//        if (officeIds == null || officeIds.size() == 0) {
//            throw new ItemException("请选择科室");
//        }
//        if (NumberUtil.isZero(questionnaireId)) {
//            throw new ItemException("请选择问卷");
//        }
//
//        QuestionnaireServiceImpl questionnaireService = SpringContext.getBean(QuestionnaireServiceImpl.class);
//        Wrapper<Questionnaire> qWhere = new EntityWrapper<>();
//        qWhere.eq(Questionnaire.ID, questionnaireId);
//        int qCount = questionnaireService.selectCount(qWhere);
//        if (qCount == 0) {
//            throw new ItemException("问卷不存在，ID：" + questionnaireId);
//        }
//    }


    public int getOfficeTypeId() {
        return officeTypeId;
    }

    public String getGroupName() {
        return groupName;
    }

    public List<Integer> getOfficeList() {
        return officeList;
    }

    public int getQuestionnaireId() {
        return questionnaireId;
    }
}
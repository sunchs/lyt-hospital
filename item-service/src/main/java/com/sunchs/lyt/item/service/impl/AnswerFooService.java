package com.sunchs.lyt.item.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.sunchs.lyt.db.business.entity.Hospital;
import com.sunchs.lyt.db.business.entity.HospitalOffice;
import com.sunchs.lyt.db.business.entity.ItemOffice;
import com.sunchs.lyt.db.business.entity.Questionnaire;
import com.sunchs.lyt.db.business.service.impl.HospitalOfficeServiceImpl;
import com.sunchs.lyt.db.business.service.impl.HospitalServiceImpl;
import com.sunchs.lyt.db.business.service.impl.ItemOfficeServiceImpl;
import com.sunchs.lyt.db.business.service.impl.QuestionnaireServiceImpl;
import com.sunchs.lyt.framework.util.ObjectUtil;
import com.sunchs.lyt.item.bean.AnswerParam;
import com.sunchs.lyt.item.bean.ItemOfficeFooData;
import com.sunchs.lyt.item.enums.OfficeTypeEnum;
import com.sunchs.lyt.item.exception.ItemException;
import com.sunchs.lyt.item.service.IAnswerFooService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class AnswerFooService implements IAnswerFooService {

    @Autowired
    private ItemOfficeServiceImpl itemOfficeService;

    @Autowired
    private HospitalOfficeServiceImpl hospitalOfficeService;

    @Autowired
    private QuestionnaireServiceImpl questionnaireService;

    @Autowired
    private HospitalServiceImpl hospitalService;

    @Override
    public void saveAnswer(AnswerParam param) {

    }

    @Override
    public ItemOfficeFooData getItemOfficeInfo(int itemId, int officeId) {
        ItemOffice itemOffice = getItemOffice(itemId, officeId);
        if (Objects.isNull(itemOffice)) {
            throw new ItemException("项目科室已停用！");
        }

        ItemOfficeFooData data = ObjectUtil.copy(itemOffice, ItemOfficeFooData.class);
        data.setHospitalName(getHospitalNameById(data.getHospitalId()));
        data.setOfficeName(getOfficeNameById(data.getOfficeId()));
        data.setOfficeTypeName(OfficeTypeEnum.get(data.getOfficeTypeId()));
        data.setQuestionnaireName(getQuestionnaireNameById(data.getQuestionnaireId()));

        return data;
    }

    private ItemOffice getItemOffice(int itemId, int officeId) {
        Wrapper<ItemOffice> wrapper = new EntityWrapper<ItemOffice>()
                .eq(ItemOffice.ITEM_ID, itemId)
                .eq(ItemOffice.OFFICE_ID, officeId);
        return itemOfficeService.selectOne(wrapper);
    }

    private String getHospitalNameById(int hospitalId) {
        Wrapper<Hospital> wrapper = new EntityWrapper<Hospital>()
                .eq(Hospital.ID, hospitalId);
        Hospital row = hospitalService.selectOne(wrapper);
        if (Objects.nonNull(row)) {
            return row.getHospitalName();
        }
        return "";
    }

    private String getOfficeNameById(int officeId) {
        Wrapper<HospitalOffice> wrapper = new EntityWrapper<HospitalOffice>()
                .eq(HospitalOffice.ID, officeId);
        HospitalOffice row = hospitalOfficeService.selectOne(wrapper);
        if (Objects.nonNull(row)) {
            return row.getTitle();
        }
        return "";
    }

    private String getQuestionnaireNameById(int questionnaireId) {
        Wrapper<Questionnaire> wrapper = new EntityWrapper<Questionnaire>()
                .eq(Questionnaire.ID, questionnaireId);
        Questionnaire row = questionnaireService.selectOne(wrapper);
        if (Objects.nonNull(row)) {
            return row.getTitle();
        }
        return "";
    }
}

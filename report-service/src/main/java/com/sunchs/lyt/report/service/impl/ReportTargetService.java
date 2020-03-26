package com.sunchs.lyt.report.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.sunchs.lyt.db.business.entity.*;
import com.sunchs.lyt.db.business.service.impl.*;
import com.sunchs.lyt.report.bean.SatisfyData;
import com.sunchs.lyt.report.bean.TotalParam;
import com.sunchs.lyt.report.service.IReportTargetService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReportTargetService implements IReportTargetService {

    @Autowired
    private ReportAnswerSatisfyServiceImpl reportAnswerSatisfyService;

    @Autowired
    private ReportAnswerQuantityServiceImpl reportAnswerQuantityService;

    @Autowired
    private QuestionTargetServiceImpl questionTargetService;

    @Autowired
    private HospitalOfficeServiceImpl hospitalOfficeService;

    @Autowired
    private SettingItemWeightServiceImpl settingItemWeightService;

    @Override
    public List<SatisfyData> getItemSatisfyByTarget(int itemId, int targetId, int position) {
        if (position == 1) {
            return getOneTargetSatisfyList(itemId, targetId);
        } else if (position == 2) {
            return getTwoTargetSatisfyList(itemId, targetId);
        }
        return new ArrayList<>();
    }

    @Override
    public List<SatisfyData> getItemOfficeSatisfy(TotalParam param) {
        List<SatisfyData> list = new ArrayList<>();
        // 查询
        Wrapper<ReportAnswerSatisfy> wrapper = new EntityWrapper<>();
        wrapper.setSqlSelect("office_id as officeId,TRUNCATE(AVG(score),0) as score", ReportAnswerSatisfy.TARGET_ONE+" as targetOne", ReportAnswerSatisfy.TARGET_TWO+" as targetTwo");
        wrapper.eq(ReportAnswerSatisfy.ITEM_ID, param.getItemId());
        wrapper.ne(ReportAnswerSatisfy.SCORE, 0);
        wrapper.eq(ReportAnswerSatisfy.TARGET_ONE, param.getTargetId());

        if (Objects.nonNull(param.getOfficeList()) && param.getOfficeList().size() > 0) {
            wrapper.in(ReportAnswerSatisfy.OFFICE_ID, param.getOfficeList());
        }
        if (Objects.nonNull(param.getTargetList()) && param.getTargetList().size() > 0) {
            wrapper.in(ReportAnswerSatisfy.TARGET_THREE, param.getTargetList());
        }

        wrapper.andNew("question_id IN (SELECT id FROM question WHERE option_type IN(1,4))");
        wrapper.groupBy(ReportAnswerSatisfy.OFFICE_ID);
        wrapper.groupBy(ReportAnswerSatisfy.TARGET_TWO);
        List<ReportAnswerSatisfy> satisfyList = reportAnswerSatisfyService.selectList(wrapper);
        satisfyList.forEach(s->{
            SatisfyData data = new SatisfyData();
            data.setOfficeId(s.getOfficeId());
            data.setOfficeName(getOfficeName(s.getOfficeId()));
            data.setpId(s.getTargetOne());
            data.setpName(getTargetName(s.getTargetOne()));
            data.setId(s.getTargetTwo());
            data.setName(getTargetName(s.getTargetTwo()));
            double value = new BigDecimal((double)s.getScore() / (double)100).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            data.setValue(value);
            list.add(data);
        });
        return list;
    }

    @Override
    public List<SatisfyData> getItemOfficeTargetSatisfy(TotalParam param) {
        List<SatisfyData> list = new ArrayList<>();
        // 查询
        Wrapper<ReportAnswerSatisfy> wrapper = new EntityWrapper<>();
        wrapper.setSqlSelect("office_id as officeId,TRUNCATE(AVG(score),0) as score",
                ReportAnswerSatisfy.TARGET_ONE+" as targetOne",
                ReportAnswerSatisfy.TARGET_TWO+" as targetTwo",
                ReportAnswerSatisfy.TARGET_THREE+" as targetThree");
        wrapper.eq(ReportAnswerSatisfy.ITEM_ID, param.getItemId());
        wrapper.ne(ReportAnswerSatisfy.SCORE, 0);
        wrapper.eq(ReportAnswerSatisfy.TARGET_ONE, param.getTargetId());

        if (Objects.nonNull(param.getOfficeList()) && param.getOfficeList().size() > 0) {
            wrapper.in(ReportAnswerSatisfy.OFFICE_ID, param.getOfficeList());
        } else {
            return list;
        }
        if (Objects.nonNull(param.getTargetList()) && param.getTargetList().size() > 0) {
            wrapper.in(ReportAnswerSatisfy.TARGET_THREE, param.getTargetList());
        } else {
            return list;
        }

        wrapper.andNew("question_id IN (SELECT id FROM question WHERE option_type IN(1,4))");
        wrapper.groupBy(ReportAnswerSatisfy.OFFICE_ID);
        wrapper.groupBy(ReportAnswerSatisfy.TARGET_THREE);
        List<ReportAnswerSatisfy> satisfyList = reportAnswerSatisfyService.selectList(wrapper);
        satisfyList.forEach(s->{
            SatisfyData data = new SatisfyData();
            data.setOfficeId(s.getOfficeId());
            data.setOfficeName(getOfficeName(s.getOfficeId()));
            data.setpId(s.getTargetTwo());
            data.setpName(getTargetName(s.getTargetTwo()));
            data.setId(s.getTargetThree());
            data.setName(getTargetName(s.getTargetThree()));
            double value = new BigDecimal((double)s.getScore() / (double)100).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            data.setValue(value);
            list.add(data);
        });
        return list;
    }

    @Override
    public Double getItemAllSatisfy(Integer itemId, Integer officeType) {
        double allScore = 0;
        Wrapper<SettingItemWeight> weightWrapper = new EntityWrapper<SettingItemWeight>()
                .eq(SettingItemWeight.ITEM_ID, itemId)
                .eq(SettingItemWeight.OFFICE_TYPE, officeType);
        List<SettingItemWeight> weightList = settingItemWeightService.selectList(weightWrapper);
        if (Objects.isNull(weightList) || weightList.size() == 0) {
            return allScore;
        }
        // 判断是否有三级指标
        List<Integer> targetIds = weightList.stream().map(SettingItemWeight::getTargetThree).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(targetIds)) {
            return allScore;
        }
        // 提取三级指标的满意度
        List<ReportAnswerQuantity> targetSatisfyList = reportAnswerQuantityService.getTargetSatisfyList(itemId, officeType, targetIds);
        Map<Integer, Double> targetSatisfyMap = targetSatisfyList.stream().collect(Collectors.toMap(ReportAnswerQuantity::getTargetThree, ReportAnswerQuantity::getSatisfyValue));

        for (SettingItemWeight weight : weightList) {
            if (targetSatisfyMap.containsKey(weight.getTargetThree())) {
                Double sVal = targetSatisfyMap.get(weight.getTargetThree());
                allScore += weight.getWeight().doubleValue() * sVal;
            }
        }
        return new BigDecimal(allScore).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    private List<SatisfyData> getOneTargetSatisfyList(int itemId, int targetId) {
        List<SatisfyData> list = new ArrayList<>();
        List<ReportAnswerQuantity> targetSatisfyList = reportAnswerQuantityService.getTargetSatisfyTwoList(itemId, targetId);
        targetSatisfyList.forEach(t -> {
            SatisfyData data = new SatisfyData();
            data.setpId(targetId);
            data.setpName(getTargetName(targetId));
            data.setId(t.getTargetTwo());
            data.setName(getTargetName(t.getTargetTwo()));
            data.setValue(t.getSatisfyValue());
            list.add(data);
        });
        return list;
    }

    private List<SatisfyData> getTwoTargetSatisfyList(int itemId, int targetId) {
        List<SatisfyData> list = new ArrayList<>();
        List<ReportAnswerQuantity> targetSatisfyList = reportAnswerQuantityService.getTargetSatisfyThreeList(itemId, targetId);
        targetSatisfyList.forEach(t -> {
            SatisfyData data = new SatisfyData();
            data.setpId(targetId);
            data.setpName(getTargetName(targetId));
            data.setId(t.getTargetThree());
            data.setName(getTargetName(t.getTargetThree()));
            data.setValue(t.getSatisfyValue());
            list.add(data);
        });
        return list;
    }

    private String getTargetName(int targetId) {
        QuestionTarget target = questionTargetService.selectById(targetId);
        return Objects.nonNull(target) ? target.getTitle() : "";
    }

    private String getOfficeName(int officeId) {
        HospitalOffice office = hospitalOfficeService.selectById(officeId);
        return Objects.nonNull(office) ? office.getTitle() : "";
    }
}

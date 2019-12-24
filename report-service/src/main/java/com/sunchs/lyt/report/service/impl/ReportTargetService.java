package com.sunchs.lyt.report.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.sunchs.lyt.db.business.entity.HospitalOffice;
import com.sunchs.lyt.db.business.entity.QuestionTarget;
import com.sunchs.lyt.db.business.entity.ReportAnswerSatisfy;
import com.sunchs.lyt.db.business.entity.SettingItemWeight;
import com.sunchs.lyt.db.business.service.impl.HospitalOfficeServiceImpl;
import com.sunchs.lyt.db.business.service.impl.QuestionTargetServiceImpl;
import com.sunchs.lyt.db.business.service.impl.ReportAnswerSatisfyServiceImpl;
import com.sunchs.lyt.db.business.service.impl.SettingItemWeightServiceImpl;
import com.sunchs.lyt.report.bean.SatisfyData;
import com.sunchs.lyt.report.bean.TotalParam;
import com.sunchs.lyt.report.service.IReportTargetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ReportTargetService implements IReportTargetService {

    @Autowired
    private ReportAnswerSatisfyServiceImpl reportAnswerSatisfyService;

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
        for (SettingItemWeight weight : weightList) {
            List<String> targetThreeStringList = Arrays.asList(weight.getTargetThree().split(","));
            List<Integer> targetThreeIds = targetThreeStringList.stream().map(v -> Integer.parseInt(v)).collect(Collectors.toList());
            double score = getSatisfyByTargetIds(itemId, officeType, targetThreeIds);
            allScore += weight.getWeight().doubleValue() * score;
        }
        allScore = allScore / (double) weightList.size();
        return new BigDecimal(allScore).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    private double getSatisfyByTargetIds(Integer itemId, Integer officeType, List<Integer> targetThreeIds) {
        double score = 0;
        Wrapper<ReportAnswerSatisfy> wrapper = new EntityWrapper<ReportAnswerSatisfy>()
                .setSqlSelect("TRUNCATE(AVG(score),0) as score")
                .eq(ReportAnswerSatisfy.ITEM_ID, itemId)
                .eq(ReportAnswerSatisfy.TARGET_ONE, officeType)
                .in(ReportAnswerSatisfy.TARGET_THREE, targetThreeIds)
                .ne(ReportAnswerSatisfy.SCORE, 0)
                .andNew("question_id IN (SELECT id FROM question WHERE option_type IN(1,4))");
        List<ReportAnswerSatisfy> satisfyList = reportAnswerSatisfyService.selectList(wrapper);
        if (Objects.isNull(satisfyList) || satisfyList.size() == 0) {
            return score;
        }
        for (ReportAnswerSatisfy reportAnswerSatisfy : satisfyList) {
            score += reportAnswerSatisfy.getScore().doubleValue();
        }
        return score / (double) satisfyList.size();
    }

    private List<SatisfyData> getOneTargetSatisfyList(int itemId, int targetId) {
        List<SatisfyData> list = new ArrayList<>();
        // 查询
        Wrapper<ReportAnswerSatisfy> wrapper = new EntityWrapper<>();
        wrapper.setSqlSelect("TRUNCATE(AVG(score),0) as score", ReportAnswerSatisfy.TARGET_ONE+" as targetOne", ReportAnswerSatisfy.TARGET_TWO+" as targetTwo");
        wrapper.eq(ReportAnswerSatisfy.ITEM_ID, itemId);
        wrapper.ne(ReportAnswerSatisfy.SCORE, 0);
        wrapper.eq(ReportAnswerSatisfy.TARGET_ONE, targetId);
        wrapper.andNew("question_id IN (SELECT id FROM question WHERE option_type IN(1,4))");
        wrapper.groupBy(ReportAnswerSatisfy.TARGET_TWO);
        List<ReportAnswerSatisfy> satisfyList = reportAnswerSatisfyService.selectList(wrapper);
        satisfyList.forEach(row->{
            SatisfyData data = new SatisfyData();
            data.setpId(row.getTargetOne());
            data.setpName(getTargetName(row.getTargetOne()));
            data.setId(row.getTargetTwo());
            data.setName(getTargetName(row.getTargetTwo()));
            double value = new BigDecimal((double)row.getScore() / (double)100).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            data.setValue(value);
            list.add(data);
        });
        return list;
    }

    private List<SatisfyData> getTwoTargetSatisfyList(int itemId, int targetId) {
        List<SatisfyData> list = new ArrayList<>();
        // 查询
        Wrapper<ReportAnswerSatisfy> wrapper = new EntityWrapper<>();
        wrapper.setSqlSelect("TRUNCATE(AVG(score),0) as score", ReportAnswerSatisfy.TARGET_TWO+" as targetTwo", ReportAnswerSatisfy.TARGET_THREE+" as targetThree");
        wrapper.eq(ReportAnswerSatisfy.ITEM_ID, itemId);
        wrapper.ne(ReportAnswerSatisfy.SCORE, 0);
        wrapper.eq(ReportAnswerSatisfy.TARGET_TWO, targetId);
        wrapper.andNew("question_id IN (SELECT id FROM question WHERE option_type IN(1,4))");
        wrapper.groupBy(ReportAnswerSatisfy.TARGET_THREE);
        List<ReportAnswerSatisfy> satisfyList = reportAnswerSatisfyService.selectList(wrapper);
        satisfyList.forEach(row->{
            SatisfyData data = new SatisfyData();
            data.setpId(row.getTargetTwo());
            data.setpName(getTargetName(row.getTargetTwo()));
            data.setId(row.getTargetThree());
            data.setName(getTargetName(row.getTargetThree()));
            double value = new BigDecimal((double)row.getScore() / (double)100).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            data.setValue(value);
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

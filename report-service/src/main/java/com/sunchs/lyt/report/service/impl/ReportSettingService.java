package com.sunchs.lyt.report.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.sunchs.lyt.db.business.entity.*;
import com.sunchs.lyt.db.business.service.impl.*;
import com.sunchs.lyt.framework.bean.TitleData;
import com.sunchs.lyt.framework.enums.OfficeTypeEnum;
import com.sunchs.lyt.report.bean.*;
import com.sunchs.lyt.report.service.IReportSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReportSettingService implements IReportSettingService {

    @Autowired
    private ReportAnswerServiceImpl reportAnswerService;

    @Autowired
    private ReportAnswerOptionServiceImpl reportAnswerOptionService;

    @Autowired
    private QuestionnaireServiceImpl questionnaireService;

    @Autowired
    private ReportAnswerQuantityServiceImpl reportAnswerQuantityService;

    @Autowired
    private QuestionTargetServiceImpl questionTargetService;

    @Autowired
    private CustomItemOfficeServiceImpl customItemOfficeService;

    @Autowired
    private CustomItemTargetServiceImpl customItemTargetService;

    @Autowired
    private SettingItemTempShowServiceImpl settingItemTempShowService;

    @Autowired
    private ReportTargetService reportTargetService;

    @Autowired
    private SettingItemWeightServiceImpl settingItemWeightService;

    @Override
    public List<TitleData> getItemUseQuestionnaireList(Integer itemId) {
        List<TitleData> result = new ArrayList<>();
        Wrapper<ReportAnswer> wrapper = new EntityWrapper<ReportAnswer>()
                .eq(ReportAnswer.ITEM_ID, itemId)
                .groupBy(ReportAnswer.QUESTIONNAIRE_ID);
        List<ReportAnswer> answerList = reportAnswerService.selectList(wrapper);
        answerList.forEach(a->{
            Questionnaire q = getQuestionnaireById(a.getQuestionnaireId());
            if (Objects.nonNull(q)) {
                TitleData data = new TitleData();
                data.setId(a.getQuestionnaireId());
                data.setType(q.getTargetOne());
                data.setTitle(q.getTitle());
                data.setSelected(false);
                result.add(data);
            }
        });
        return result;
    }

    @Override
    public List<Map<String, Object>> getItemUseAllList(Integer itemId) {
        List<Map<String, Object>> result = new ArrayList<>();
        List<TitleData> itemUseQuestionnaireList = getItemUseQuestionnaireList(itemId);
        Map<Integer, List<TitleData>> groupList = itemUseQuestionnaireList.stream().collect(Collectors.groupingBy(TitleData::getType));
        for (Integer type : groupList.keySet()) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", type);
            map.put("title", OfficeTypeEnum.get(type));
            // 获取列表
            List<TitleData> sonList = groupList.get(type);
            map.put("children", getQuestionList(itemId, sonList));
            result.add(map);
        }
        return result;
    }

    @Override
    public List<Map<String, Object>> getItemQuestionnaireUseTarget(Integer itemId, Integer questionnaireId) {
        List<Map<String, Object>> result = new ArrayList<>();
        Wrapper<ReportAnswerQuantity> wrapper = new EntityWrapper<ReportAnswerQuantity>()
                .eq(ReportAnswerQuantity.ITEM_ID, itemId)
                .eq(ReportAnswerQuantity.QUESTIONNAIRE_ID, questionnaireId);
        List<ReportAnswerQuantity> list = reportAnswerQuantityService.selectList(wrapper);
        Set<Integer> targetIds = new HashSet<>();
        list.forEach(t->{
            targetIds.add(t.getTargetOne());
            targetIds.add(t.getTargetTwo());
            targetIds.add(t.getTargetThree());
        });
        Wrapper<QuestionTarget> targetWrapper = new EntityWrapper<QuestionTarget>()
                .setSqlSelect(QuestionTarget.ID, QuestionTarget.TITLE)
                .in(QuestionTarget.ID, targetIds);
        List<QuestionTarget> targetList = questionTargetService.selectList(targetWrapper);
        Map<Integer, String> targetMap = targetList.stream().collect(Collectors.toMap(QuestionTarget::getId, QuestionTarget::getTitle));

        Map<Integer, List<ReportAnswerQuantity>> oneList = list.stream().collect(Collectors.groupingBy(ReportAnswerQuantity::getTargetOne));
        for (Integer oneId : oneList.keySet()) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", oneId);
            map.put("title", targetMap.get(oneId));
            List<ReportAnswerQuantity> twoTempList = oneList.get(oneId);
            if (Objects.nonNull(twoTempList) && twoTempList.size() > 0) {
                map.put("children", getTwoTarget(twoTempList, targetMap));
            }
            result.add(map);
        }
        return result;
    }

    @Override
    public void saveCustomItemOfficeSetting(CustomItemOfficeSettingParam param) {
//        // 清理历史数据
//        Wrapper<CustomItemOffice> customItemOfficeWrapper = new EntityWrapper<CustomItemOffice>()
//                .eq(CustomItemOffice.ITEM_ID, param.getItemId());
//        customItemOfficeService.delete(customItemOfficeWrapper);
//        // 清理历史数据
//        Wrapper<CustomItemTarget> customItemTargetWrapper = new EntityWrapper<CustomItemTarget>()
//                .eq(CustomItemTarget.ITEM_ID, param.getItemId());
//        customItemTargetService.delete(customItemTargetWrapper);

        // 写入新数据
        param.getCustomList().forEach(row->{
            if ( ! row.getTitle().equals("") && row.getOptionId() > 0 && row.getQuestionId() > 0 &&
                    row.getQuestionnaireId() > 0 && Objects.nonNull(row.getTargetList()) && row.getTargetList().size() > 0) {
                CustomItemOffice data = new CustomItemOffice();
                data.setItemId(param.getItemId());
                data.setOfficeType(row.getOfficeType());
                data.setTitle(row.getTitle());
                data.setQuestionnaireId(row.getQuestionnaireId());
                data.setQuestionId(row.getQuestionId());
                data.setOptionId(row.getOptionId());
                if (customItemOfficeService.insert(data)) {
                    row.getTargetList().forEach(t -> {
                        if (t.getTargetThree() > 0) {
                            CustomItemTarget target = new CustomItemTarget();
                            target.setCustomId(data.getId());
                            target.setItemId(data.getItemId());
                            target.setTargetOne(t.getTargetOne());
                            target.setTargetTwo(t.getTargetTwo());
                            target.setTargetThree(t.getTargetThree());
                            customItemTargetService.insert(target);
                        }
                    });
                }
            }
        });
    }

    @Override
    public void deleteCustomItemOfficeSetting(Integer id) {
        if (customItemOfficeService.deleteById(id)) {
            Wrapper<CustomItemTarget> customItemTargetWrapper = new EntityWrapper<CustomItemTarget>()
                    .eq(CustomItemTarget.CUSTOM_ID, id);
            customItemTargetService.delete(customItemTargetWrapper);
        }
    }

    @Override
    public void saveTempItemOfficeSetting(TempItemOfficeSettingParam param) {
//        // 清理历史数据
//        Wrapper<SettingItemTempShow> settingItemTempShowWrapper = new EntityWrapper<SettingItemTempShow>()
//                .eq(SettingItemTempShow.ITEM_ID, param.getItemId());
//        settingItemTempShowService.delete(settingItemTempShowWrapper);

        // 写入新数据
        param.getValueList().forEach(row->{
            if (Objects.nonNull(row.getOfficeList()) && row.getOfficeList().size() > 0 &&
                    Objects.nonNull(row.getTargetList()) && row.getTargetList().size() > 0) {
                SettingItemTempShow data = new SettingItemTempShow();
                data.setItemId(param.getItemId());
                data.setOfficeType(row.getOfficeType());
                data.setOfficeIds(String.join(",", row.getOfficeList().stream().map(v->v+"").collect(Collectors.toList())));
                data.setTargetIds(String.join(",", row.getTargetList().stream().map(v->v+"").collect(Collectors.toList())));
                settingItemTempShowService.insert(data);
            }
        });
    }

    @Override
    public List<TempOfficeData> getItemTempOfficeList(Integer itemId, Integer officeType) {
        List<TempOfficeData> result =  new ArrayList<>();
        Wrapper<SettingItemTempShow> wrapper = new EntityWrapper<SettingItemTempShow>()
                .eq(SettingItemTempShow.ITEM_ID, itemId)
                .eq(SettingItemTempShow.OFFICE_TYPE, officeType);
        List<SettingItemTempShow> settingList = settingItemTempShowService.selectList(wrapper);
        settingList.forEach(setting -> {
            TempOfficeData data = new TempOfficeData();
            data.setId(setting.getId());
            List<String> officeIdsString = Arrays.asList(setting.getOfficeIds().split(","));
            List<Integer> officeIds = officeIdsString.stream().map(v -> Integer.parseInt(v)).collect(Collectors.toList());
            List<String> targetIdsString = Arrays.asList(setting.getTargetIds().split(","));
            List<Integer> targetIds = targetIdsString.stream().map(v -> Integer.parseInt(v)).collect(Collectors.toList());

            // 查询数据
            TotalParam param = new TotalParam();
            param.setItemId(itemId);
            param.setTargetId(officeType);
            param.setOfficeList(officeIds);
            param.setTargetList(targetIds);
            List<SatisfyData> satisfyList = reportTargetService.getItemOfficeTargetSatisfy(param);
            // 科室名称MAP
            Map<Integer, String> officeMap = new HashMap<>();
            // 科室名称MAP
            Map<Integer, String> targetMap = new HashMap<>();

            List<OfficeTargetSatisfyData> valueList = new ArrayList<>();
            satisfyList.forEach(s->{
                // 抽取科室名称
                officeMap.put(s.getOfficeId(), s.getOfficeName());
                // 抽取指标名称
                targetMap.put(s.getId(), s.getName());
                // 构建新对象
                OfficeTargetSatisfyData row = new OfficeTargetSatisfyData();
                row.setOfficeId(s.getOfficeId());
                row.setTargetId(s.getId());
                row.setValue(s.getValue());
                valueList.add(row);
            });
            data.setSatisfyList(valueList);

            List<TitleData> officeList = new ArrayList<>();
            for (Integer oId : officeMap.keySet()) {
                TitleData d = new TitleData();
                d.setId(oId);
                d.setTitle(officeMap.get(oId));
                officeList.add(d);
            }
            data.setOfficeList(officeList);

            List<TitleData> targetList = new ArrayList<>();
            for (Integer oId : targetMap.keySet()) {
                TitleData d = new TitleData();
                d.setId(oId);
                d.setTitle(targetMap.get(oId));
                targetList.add(d);
            }
            data.setTargetList(targetList);
            result.add(data);
        });
        return result;
    }

    @Override
    public void deleteItemTempOffice(Integer id) {
        settingItemTempShowService.deleteById(id);
    }

    @Override
    public List<Map<String, Object>> getItemTargetList(Integer itemId, Integer officeType) {
        List<Map<String, Object>> result = new ArrayList<>();
        Wrapper<ReportAnswerOption> wrapper = new EntityWrapper<ReportAnswerOption>()
                .eq(ReportAnswerOption.ITEM_ID, itemId)
                .in(ReportAnswerOption.OPTION_TYPE, Arrays.asList(1,4))
                .groupBy(ReportAnswerOption.TARGET_THREE);
        if (Objects.nonNull(officeType) && officeType > 0) {
            wrapper.eq(ReportAnswerOption.OFFICE_TYPE_ID, officeType);
        }
        List<ReportAnswerOption> optionList = reportAnswerOptionService.selectList(wrapper);
        Map<Integer, List<ReportAnswerOption>> oneGroup = optionList.stream().collect(Collectors.groupingBy(ReportAnswerOption::getTargetOne));
        for (Integer oneId : oneGroup.keySet()) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", oneId);
            map.put("title", OfficeTypeEnum.get(oneId));
            List<ReportAnswerOption> threeList = oneGroup.get(oneId);
            if (Objects.nonNull(oneGroup) && oneGroup.size() > 0) {
                map.put("children", getTwoTargetMap(threeList));
            }
            result.add(map);
        }
        return result;
    }

    @Override
    public void saveItemAllSatisfySetting(ItemAllSatisfySettingParam param) {
        if (Objects.nonNull(param.getValueList()) && param.getValueList().size() > 0) {
            param.getValueList().forEach(item->{
                if (Objects.nonNull(item.getTargetThree()) && item.getTargetThree().size() > 0) {
                    SettingItemWeight data = new SettingItemWeight();
                    data.setItemId(param.getItemId());
                    data.setOfficeType(item.getOfficeType());
                    data.setTargetTwo(item.getTargetTwo());
                    data.setWeight(item.getWeight());
                    data.setTargetThree(String.join(",", item.getTargetThree().stream().map(v->v+"").collect(Collectors.toList())));
                    settingItemWeightService.insert(data);
                }
            });
        }
    }

    @Override
    public void deleteItemAllSatisfySetting(Integer id) {
        settingItemWeightService.deleteById(id);
    }

    @Override
    public List<Map<String, Object>> getItemAllSatisfySettingList(Integer itemId, Integer officeType) {
        List<Map<String, Object>> result = new ArrayList<>();
        Wrapper<SettingItemWeight> weightWrapper = new EntityWrapper<SettingItemWeight>()
                .eq(SettingItemWeight.ITEM_ID, itemId)
                .eq(SettingItemWeight.OFFICE_TYPE, officeType);
        List<SettingItemWeight> itemWeightList = settingItemWeightService.selectList(weightWrapper);
        List<Integer> targetIds = new ArrayList<>();
        itemWeightList.forEach(row->{
            targetIds.add(row.getTargetTwo());
            List<String> threeList = Arrays.asList(row.getTargetThree().split(","));
            threeList.forEach(id->{
                targetIds.add(Integer.parseInt(id));
            });
        });
        Wrapper<QuestionTarget> targetWrapper = new EntityWrapper<QuestionTarget>()
                .eq(QuestionTarget.ID, targetIds);
        List<QuestionTarget> targetList = questionTargetService.selectList(targetWrapper);
        Map<Integer, String> targetTitleMap = targetList.stream().collect(Collectors.toMap(QuestionTarget::getId, QuestionTarget::getTitle));
        itemWeightList.forEach(row->{
            Map<String, Object> map = new HashMap<>();
            map.put("id", row.getId());
            map.put("title", targetTitleMap.get(row.getId()));
            map.put("officeType", row.getOfficeType());
            map.put("weight", row.getWeight());
            List<Map<String, Object>> sList = new ArrayList<>();
            List<String> threeList = Arrays.asList(row.getTargetThree().split(","));
            threeList.forEach(id->{
                Map<String, Object> m = new HashMap<>();
                m.put("id", Integer.parseInt(id));
                m.put("title", targetTitleMap.get(Integer.parseInt(id)));
                sList.add(m);
            });
            if (Objects.nonNull(threeList) && threeList.size() > 0) {
                map.put("children", sList);
            }
            result.add(map);
        });
        return result;
    }

    private List<Map<String, Object>> getTwoTargetMap(List<ReportAnswerOption> twoTempList) {
        List<Map<String, Object>> result = new ArrayList<>();
        Map<Integer, List<ReportAnswerOption>> twoList = twoTempList.stream().collect(Collectors.groupingBy(ReportAnswerOption::getTargetTwo));
        Map<Integer, String> targetNameMap = getTargetNameByIds(twoTempList.stream().map(ReportAnswerOption::getTargetTwo).collect(Collectors.toSet()));
        for (Integer towId : twoList.keySet()) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", towId);
            map.put("title", targetNameMap.get(towId));
            List<ReportAnswerOption> threeList = twoList.get(towId);
            if (Objects.nonNull(threeList) && threeList.size() > 0) {
                map.put("children", getThreeTargetMap(threeList));
            }
            result.add(map);
        }
        return result;
    }

    private List<Map<String, Object>> getThreeTargetMap(List<ReportAnswerOption> list) {
        Map<Integer, List<ReportAnswerOption>> groupList = list.stream().collect(Collectors.groupingBy(ReportAnswerOption::getTargetThree));
        Map<Integer, String> targetNameMap = getTargetNameByIds(list.stream().map(ReportAnswerOption::getTargetThree).collect(Collectors.toSet()));
        List<Map<String, Object>> result = new ArrayList<>();
        for (Integer threeId : groupList.keySet()) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", threeId);
            map.put("title", targetNameMap.get(threeId));
            result.add(map);
        }
        return result;
    }

    private List<Map<String, Object>> getTwoTarget(List<ReportAnswerQuantity> twoTempList, Map<Integer, String> targetMap) {
        List<Map<String, Object>> result = new ArrayList<>();
        Map<Integer, List<ReportAnswerQuantity>> twoList = twoTempList.stream().collect(Collectors.groupingBy(ReportAnswerQuantity::getTargetTwo));
        for (Integer towId : twoList.keySet()) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", towId);
            map.put("title", targetMap.get(towId));
            List<ReportAnswerQuantity> threeList = twoList.get(towId);
            if (Objects.nonNull(threeList) && threeList.size() > 0) {
                map.put("children", getThreeTarget(threeList, targetMap));
            }
            result.add(map);
        }
        return result;
    }

    private List<Map<String, Object>> getThreeTarget(List<ReportAnswerQuantity> list, Map<Integer, String> targetMap) {
        Map<Integer, List<ReportAnswerQuantity>> groupList = list.stream().collect(Collectors.groupingBy(ReportAnswerQuantity::getTargetThree));
        List<Map<String, Object>> result = new ArrayList<>();
        for (Integer threeId : groupList.keySet()) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", threeId);
            map.put("title", targetMap.get(threeId));
            result.add(map);
        }
        return result;
    }

    /**
     * 获取问卷名称
     */
    private Questionnaire getQuestionnaireById(Integer questionnaireId) {
        Questionnaire row = questionnaireService.selectById(questionnaireId);
        if (Objects.nonNull(row)) {
            return row;
        }
        return null;
    }

    private List<Map<String, Object>> getQuestionList(Integer itemId, List<TitleData> list) {
        List<Map<String, Object>> result = new ArrayList<>();
        list.forEach(wj->{
            Map<String, Object> map = new HashMap<>();
            map.put("id", wj.getId());
            map.put("title", wj.getTitle());
            Wrapper<ReportAnswerOption> wrapper = new EntityWrapper<ReportAnswerOption>()
                    .eq(ReportAnswerOption.ITEM_ID, itemId)
                    .eq(ReportAnswerOption.QUESTIONNAIRE_ID, wj.getId())
                    .groupBy(ReportAnswerOption.QUESTION_ID);
            List<ReportAnswerOption> answerOptionList = reportAnswerOptionService.selectList(wrapper);
            map.put("children", getOptionList(itemId, answerOptionList));
            result.add(map);
        });
        return result;
    }

    private List<Map<String, Object>> getOptionList(Integer itemId, List<ReportAnswerOption> answerOptionList) {
        List<Map<String, Object>> result = new ArrayList<>();
        answerOptionList.forEach(a->{
            Map<String, Object> map = new HashMap<>();
            map.put("id", a.getQuestionId());
            map.put("title", a.getQuestionName());

            List<Map<String, Object>> oList = new ArrayList<>();
            Wrapper<ReportAnswerOption> wrapper = new EntityWrapper<ReportAnswerOption>()
                    .eq(ReportAnswerOption.ITEM_ID, itemId)
                    .eq(ReportAnswerOption.QUESTIONNAIRE_ID, a.getQuestionnaireId())
                    .eq(ReportAnswerOption.QUESTION_ID, a.getQuestionId())
                    .groupBy(ReportAnswerOption.OPTION_ID);
            List<ReportAnswerOption> optionList = reportAnswerOptionService.selectList(wrapper);
            optionList.forEach(b->{
                Map<String, Object> m = new HashMap<>();
                m.put("id", b.getOptionId());
                m.put("title", b.getOptionName());
                oList.add(m);
            });
            map.put("children", oList);
            result.add(map);
        });
        return result;
    }

    private Map<Integer, String> getTargetNameByIds(Set<Integer> targetIds) {
        Wrapper<QuestionTarget> targetWrapper = new EntityWrapper<QuestionTarget>()
                .setSqlSelect(QuestionTarget.ID, QuestionTarget.TITLE)
                .in(QuestionTarget.ID, targetIds);
        List<QuestionTarget> targetList = questionTargetService.selectList(targetWrapper);
        Map<Integer, String> targetMap = targetList.stream().collect(Collectors.toMap(QuestionTarget::getId, QuestionTarget::getTitle));
        return targetMap;
    }
}

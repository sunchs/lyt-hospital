package com.sunchs.lyt.report.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.google.common.collect.Lists;
import com.sunchs.lyt.db.business.entity.*;
import com.sunchs.lyt.db.business.mapper.SettingItemTempShowMapper;
import com.sunchs.lyt.db.business.service.impl.*;
import com.sunchs.lyt.framework.bean.TitleData;
import com.sunchs.lyt.framework.bean.TitleValueChildrenData;
import com.sunchs.lyt.framework.bean.TitleValueData;
import com.sunchs.lyt.framework.enums.OfficeTypeEnum;
import com.sunchs.lyt.framework.util.UserThreadUtil;
import com.sunchs.lyt.report.bean.*;
import com.sunchs.lyt.report.exception.ReportException;
import com.sunchs.lyt.report.service.IReportSettingService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.math.BigDecimal;
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

    @Autowired
    private ItemOfficeServiceImpl itemOfficeService;

    @Autowired
    private ItemTempOfficeServiceImpl itemTempOfficeService;

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
        // 参数判断
        List<Integer> officeIds = new ArrayList<>();
        param.getValueList().forEach(row->{
            if (Objects.nonNull(row.getOfficeId()) && row.getOfficeId() > 0 && CollectionUtils.isNotEmpty(row.getTargetList())) {
                officeIds.add(row.getOfficeId());
            }
        });
        if (CollectionUtils.isEmpty(officeIds)) {
            throw new ReportException("请选择科室和指标!");
        }
        if (officeIds.size() != officeIds.stream().distinct().count()) {
            throw new ReportException("科室不能重复!");
        }
        // 判断是否存在数据库
        Wrapper<ItemTempOffice> tempOfficeWrapper = new EntityWrapper<ItemTempOffice>()
                .setSqlSelect(ItemTempOffice.OFFICE_ID.concat(" as officeId"))
                .eq(ItemTempOffice.ITEM_ID, param.getItemId())
                .eq(ItemTempOffice.OFFICE_TYPE, param.getOfficeType())
                .in(ItemTempOffice.OFFICE_ID, officeIds);
        List<ItemTempOffice> itemTempOffices = itemTempOfficeService.selectList(tempOfficeWrapper);
        if (CollectionUtils.isNotEmpty(itemTempOffices)) {
            List<Integer> oIds = itemTempOffices.stream().map(ItemTempOffice::getOfficeId).collect(Collectors.toList());
            Wrapper<ItemOffice> itemOfficeWrapper = new EntityWrapper<ItemOffice>()
                    .setSqlSelect(
                        ItemOffice.TITLE,
                        ItemOffice.GROUP_NAME.concat(" as groupName")
                    )
                    .eq(ItemOffice.ITEM_ID, param.getItemId())
                    .eq(ItemOffice.OFFICE_TYPE_ID, param.getOfficeType())
                    .in(ItemOffice.OFFICE_ID, oIds);
            List<ItemOffice> itemOffices = itemOfficeService.selectList(itemOfficeWrapper);
            String tip = "";
            for (ItemOffice o : itemOffices) {
                tip += o.getTitle().equals("") ? "：" + o.getGroupName() : "：" + o.getTitle();
            }
            throw new ReportException("科室已存在" + tip);
        }
        // 保存数据
        param.getValueList().forEach(row->{
            if (Objects.nonNull(row.getOfficeId()) && row.getOfficeId() > 0 && CollectionUtils.isNotEmpty(row.getTargetList())) {
                ItemTempOffice data = new ItemTempOffice();
                data.setItemId(param.getItemId());
                data.setOfficeType(param.getOfficeType());
                data.setOfficeId(row.getOfficeId());
                data.setTargetIds(String.join(",", row.getTargetList().stream().map(v->v+"").collect(Collectors.toList())));
                data.setCreateId(UserThreadUtil.getUserId());
                data.setCreateTime(new Date());
                itemTempOfficeService.insert(data);
            }
        });
    }

    @Override
    public TempOfficeDataVO getItemTempOfficeList(Integer itemId, Integer officeType) {
        List<TempOfficeData> list =  new ArrayList<>();
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
            list.add(data);
        });
        // 赋值
        TempOfficeDataVO vo = new TempOfficeDataVO();
        vo.setList(list);
        // 排名
        List<TitleValueDataVO> rankingList = new ArrayList<>();
        list.forEach(l -> {
            List<OfficeTargetSatisfyData> satisfyList = l.getSatisfyList();
            List<TitleData> officeList = l.getOfficeList();
            officeList.forEach(o -> {
                // 获取单科室的满意度
                List<OfficeTargetSatisfyData> satisfyData = satisfyList.stream().filter(v -> v.getOfficeId().equals(o.getId())).collect(Collectors.toList());
                double allScore = 0;
                int number = 0;
                for (OfficeTargetSatisfyData t : satisfyData) {
                    if (t.getValue() > 0) {
                        allScore += t.getValue();
                        number++;
                    }
                }
                double value = new BigDecimal(allScore / (double)number).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                TitleValueDataVO d = new TitleValueDataVO();
                d.setId(o.getId());
                d.setTitle(o.getTitle());
                d.setValue(value);
                rankingList.add(d);
            });
        });
        // 排序
        rankingList.sort(Comparator.comparing(TitleValueDataVO::getValue).reversed());
        // 排序次数过滤
        int rank = 0;
        double rankValue = 0;
        int tempRank = 0;
        for (TitleValueDataVO t : rankingList) {
            if (rank == 0) {
                rank++;
                rankValue = t.getValue();
                t.setRankValue(rank);
            } else if (rankValue != t.getValue()) {
                rank++;
                rankValue = t.getValue();
                rank += tempRank;
                tempRank = 0;
            } else {
                tempRank++;
            }
            t.setRankValue(rank);
        }
        vo.setRankingList(rankingList);
        return vo;
    }

    @Override
    public void deleteItemTempOffice(Integer id) {
        itemTempOfficeService.deleteById(id);
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
        // 清理历史数据
        deleteItemAllSatisfySetting(param.getItemId(), param.getOfficeType());
        // 插入新数据
        if (Objects.nonNull(param.getValueList()) && param.getValueList().size() > 0) {
            param.getValueList().forEach(row->{
                if (Objects.nonNull(row.getTargetTwo()) && Objects.nonNull(row.getTargetThree())) {
                    SettingItemWeight data = new SettingItemWeight();
                    data.setItemId(param.getItemId());
                    data.setOfficeType(param.getOfficeType());
                    data.setTargetTwo(row.getTargetTwo());
                    data.setTargetThree(row.getTargetThree());
                    data.setWeight(row.getWeight());
                    settingItemWeightService.insert(data);
                }
            });
        }
    }

    @Override
    public void deleteItemAllSatisfySetting(Integer itemId, Integer officeType) {
        Wrapper<SettingItemWeight> weightWrapper = new EntityWrapper<SettingItemWeight>()
                .eq(SettingItemWeight.ITEM_ID, itemId)
                .eq(SettingItemWeight.OFFICE_TYPE, officeType);
        settingItemWeightService.delete(weightWrapper);
    }

    @Override
    public List<Map<String, Object>> getItemAllSatisfySettingList(Integer itemId, Integer officeType) {
        List<Map<String, Object>> result = new ArrayList<>();
        Wrapper<SettingItemWeight> weightWrapper = new EntityWrapper<SettingItemWeight>()
                .eq(SettingItemWeight.ITEM_ID, itemId)
                .eq(SettingItemWeight.OFFICE_TYPE, officeType);
        List<SettingItemWeight> itemWeightList = settingItemWeightService.selectList(weightWrapper);
        Set<Integer> targetIds = new HashSet<>();
        itemWeightList.forEach(row->{
            targetIds.add(row.getTargetTwo());
            targetIds.add(row.getTargetThree());
        });
        // 题目指标内容
        Wrapper<QuestionTarget> targetWrapper = new EntityWrapper<QuestionTarget>()
                .in(QuestionTarget.ID, targetIds);
        List<QuestionTarget> targetList = questionTargetService.selectList(targetWrapper);
        Map<Integer, String> targetTitleMap = targetList.stream().collect(Collectors.toMap(QuestionTarget::getId, QuestionTarget::getTitle));
        // 组合数据
        Map<Integer, List<SettingItemWeight>> groupMap = itemWeightList.stream().collect(Collectors.groupingBy(SettingItemWeight::getTargetTwo));
        for (Integer twoKey : groupMap.keySet()) {
            List<SettingItemWeight> settingItemWeights = groupMap.get(twoKey);
            // 二级指标内容
            Map<String, Object> map = new HashMap<>();
            map.put("id", twoKey);
            map.put("title", targetTitleMap.get(twoKey));
            // 三级指标内容
            List<Map<String, Object>> sList = new ArrayList<>();
            for (SettingItemWeight w : settingItemWeights) {
                Map<String, Object> m = new HashMap<>();
                m.put("id", w.getTargetThree());
                m.put("title", targetTitleMap.get(w.getTargetThree()));
                m.put("weight", w.getWeight());
                sList.add(m);
            }
            if (Objects.nonNull(sList) && sList.size() > 0) {
                map.put("children", sList);
            }
            result.add(map);
        }
        return result;
    }

    @Override
    public List<TitleChildrenVO> getItemTempOfficeChildren(Integer itemId) {
        List<TitleChildrenVO> result = new ArrayList<>();
        Wrapper<ItemTempOffice> tempOfficeWrapper = new EntityWrapper<ItemTempOffice>()
                .eq(ItemTempOffice.ITEM_ID, itemId);
        List<ItemTempOffice> tempOfficeList = itemTempOfficeService.selectList(tempOfficeWrapper);
        Map<Integer, List<ItemTempOffice>> tempMap = tempOfficeList.stream().collect(Collectors.groupingBy(ItemTempOffice::getOfficeType));
        for (Integer officeType : tempMap.keySet()) {
            List<ItemTempOffice> tempOfficeGroup = tempMap.get(officeType);
            List<Integer> oIds = tempOfficeGroup.stream().map(ItemTempOffice::getOfficeId).collect(Collectors.toList());
            Wrapper<ItemOffice> itemOfficeWrapper = new EntityWrapper<ItemOffice>()
                    .eq(ItemOffice.ITEM_ID, itemId)
                    .eq(ItemOffice.OFFICE_TYPE_ID, officeType)
                    .in(ItemOffice.OFFICE_ID, oIds);
            List<ItemOffice> itemOfficeList = itemOfficeService.selectList(itemOfficeWrapper);
            // 消息体
            TitleChildrenVO data = new TitleChildrenVO();
            data.setId(officeType);
            data.setTitle(OfficeTypeEnum.get(officeType));
            // 提取项目科室列表
            if (CollectionUtils.isNotEmpty(itemOfficeList)) {
                List<TitleChildrenVO> childList = new ArrayList<>();
                itemOfficeList.forEach(office -> {
                    TitleChildrenVO vo = new TitleChildrenVO();
                    vo.setId(office.getOfficeId());
                    if (office.getOfficeTypeId().equals(3) && office.getTitle().equals("")) {
                        vo.setTitle("员工");
                    } else {
                        vo.setTitle(office.getTitle());
                    }
                    childList.add(vo);
                });
                data.setChildren(childList);
            }
            result.add(data);
        }
        return result;
    }

    @Override
    public List<TitleChildrenVO> getItemTempOfficeSettingV2(Integer itemId, Integer officeType) {
        List<TitleChildrenVO> result = new ArrayList<>();
        // 获取数据
        Wrapper<ItemTempOffice> tempOfficeWrapper = new EntityWrapper<ItemTempOffice>()
                .eq(ItemTempOffice.ITEM_ID, itemId)
                .eq(ItemTempOffice.OFFICE_TYPE, officeType);
        List<ItemTempOffice> itemTempOffices = itemTempOfficeService.selectList(tempOfficeWrapper);
        List<Integer> officeIds = itemTempOffices.stream().map(ItemTempOffice::getOfficeId).collect(Collectors.toList());
        Map<Integer, String> officeNameMap = getItemTempOfficeNameMap(itemId, officeType, officeIds);
        // 组合数据集合
        itemTempOffices.forEach(temp -> {
            TitleChildrenVO data = new TitleChildrenVO();
            data.setId(temp.getId());
            data.setTitle(officeNameMap.get(temp.getOfficeId()));
            // 拆分指标
            List<TitleChildrenVO> childList = new ArrayList<>();
            String[] split = temp.getTargetIds().split(",");
            List<String> targetList = Arrays.asList(split);
            List<Integer> targetIds = targetList.stream().map(v -> Integer.parseInt(v)).collect(Collectors.toList());
            Map<Integer, String> targetNameMap = getTargetNameByIds(targetIds);
            targetIds.forEach(targetId -> {
                TitleChildrenVO ch = new TitleChildrenVO();
                ch.setTitle(targetNameMap.get(targetId));
                ch.setChildren(new ArrayList<>());
                childList.add(ch);
            });
            data.setChildren(childList);
            result.add(data);
        });
        return result;
    }

    @Override
    public List<TitleValueChildrenData> getItemTempOfficeSatisfyList(Integer itemId, Integer officeType) {
        List<TitleValueChildrenData> result = new ArrayList<>();
        List<ItemTempOffice> settingList = getItemTempOfficeSettingList(itemId, officeType);
        Set<Integer> officeIds = settingList.stream().map(ItemTempOffice::getOfficeId).collect(Collectors.toSet());
        Map<Integer, String> officeNameMap = getOfficeNameByIds(itemId, officeType, officeIds);
        settingList.forEach(setting -> {
            if (setting.getOfficeId() > 0 && CollectionUtils.isNotEmpty(setting.getTargetList())) {
                // 结果
                TitleValueChildrenData data = new TitleValueChildrenData();
                data.setId(setting.getOfficeId());
                data.setTitle(officeNameMap.get(setting.getOfficeId()));
                // 提取指标满意度
                List<TitleValueData> targetList = new ArrayList<>();
                List<ReportAnswerQuantity> satisfyList = reportAnswerQuantityService
                        .getItemOfficeTargetSatisfyList(itemId, officeType, setting.getOfficeId(), setting.getTargetList());
                List<Integer> targetIds = satisfyList.stream().map(ReportAnswerQuantity::getTargetThree).collect(Collectors.toList());
                Map<Integer, String> targetNameMap = getTargetNameByIds(targetIds);
                satisfyList.forEach(row -> {
                    TitleValueData vo = new TitleValueData();
                    vo.setId(row.getTargetThree());
                    vo.setTitle(targetNameMap.get(row.getTargetThree()));
                    vo.setValue(row.getSatisfyValue());
                    targetList.add(vo);
                });
                data.setChildren(targetList);
                // 科室满意度均值
                Double officeSatisfyValue = targetList.stream().collect(Collectors.averagingDouble(TitleValueData::getValue));
                data.setValue(officeSatisfyValue);
                result.add(data);
            }
        });
        return result;
    }

    @Override
    public Map<String, Object> getItemTempOfficeSatisfyAndRankingList(Integer itemId, Integer officeType) {
        Map<String, Object> map = new HashMap<>();
        List<TitleValueChildrenData> satisfyList = getItemTempOfficeSatisfyList(itemId, officeType);
        map.put("list", satisfyList);

        // 排序
        satisfyList.sort(Comparator.comparing(TitleValueChildrenData::getValue).reversed());
        // 排序次数过滤
        int rank = 0;
        double rankValue = 0;
        int tempRank = 0;
        for (TitleValueChildrenData t : satisfyList) {
            if (rank == 0) {
                rank++;
                rankValue = t.getValue();
                t.setId(rank);
            } else if (rankValue != t.getValue()) {
                rank++;
                rankValue = t.getValue();
                rank += tempRank;
                tempRank = 0;
            } else {
                tempRank++;
            }
            t.setId(rank);
        }
        map.put("rankingList", satisfyList);
        return map;
    }

    @Override
    public List<ItemTempOffice> getItemTempOfficeSettingList(Integer itemId, Integer officeType) {
        Wrapper<ItemTempOffice> tempOfficeWrapper = new EntityWrapper<ItemTempOffice>()
                .eq(ItemTempOffice.ITEM_ID, itemId);
        if ( ! officeType.equals(0)) {
            tempOfficeWrapper.eq(ItemTempOffice.OFFICE_TYPE, officeType);
        }
        List<ItemTempOffice> settingList = itemTempOfficeService.selectList(tempOfficeWrapper);
        settingList.forEach(setting -> {
            // 拆分为指标ID集合
            List<String> targetIdsString = Arrays.asList(setting.getTargetIds().split(","));
            List<Integer> targetIds = targetIdsString.stream().map(v -> Integer.parseInt(v)).collect(Collectors.toList());
            setting.setTargetList(targetIds);
        });
        return settingList;
    }

    private Map<Integer, String> getItemTempOfficeNameMap(Integer itemId, Integer officeType, List<Integer> officeIds) {
        Map<Integer, String> map = new HashMap<>();
        Wrapper<ItemOffice> itemOfficeWrapper = new EntityWrapper<ItemOffice>()
                .setSqlSelect(
                        ItemOffice.OFFICE_ID.concat(" as officeId"),
                        ItemOffice.TITLE,
                        ItemOffice.GROUP_NAME.concat(" as groupName")
                )
                .eq(ItemOffice.ITEM_ID, itemId)
                .eq(ItemOffice.OFFICE_TYPE_ID, officeType)
                .in(ItemOffice.OFFICE_ID, officeIds);
        List<ItemOffice> itemOffices = itemOfficeService.selectList(itemOfficeWrapper);
        itemOffices.forEach(o -> {
            map.put(o.getOfficeId(), o.getTitle().equals("") ? o.getGroupName() : o.getTitle());
        });
        return map;
    }

    /**
     * 根据 指标ID集合 获取指标名称
     */
    private Map<Integer, String> getTargetNameByIds(List<Integer> targetIds) {
        Wrapper<QuestionTarget> wrapper = new EntityWrapper<QuestionTarget>()
                .setSqlSelect(QuestionTarget.ID, QuestionTarget.TITLE)
                .in(QuestionTarget.ID, targetIds);
        List<QuestionTarget> itemList = questionTargetService.selectList(wrapper);
        return itemList.stream().collect(Collectors.toMap(QuestionTarget::getId, QuestionTarget::getTitle));
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

    private Map<Integer, String> getOfficeNameByIds(Integer itemId, Integer officeType, Set<Integer> officeIds) {
        Wrapper<ItemOffice> wrapper = new EntityWrapper<ItemOffice>()
                .setSqlSelect(
                        ItemOffice.OFFICE_ID.concat(" as officeId"),
                        ItemOffice.TITLE,
                        ItemOffice.GROUP_NAME.concat(" as groupName")
                )
                .eq(ItemOffice.ITEM_ID, itemId)
                .eq(ItemOffice.OFFICE_TYPE_ID, officeType)
                .in(ItemOffice.OFFICE_ID, officeIds);
        List<ItemOffice> itemOfficeList = itemOfficeService.selectList(wrapper);
        Map<Integer, String> targetMap = new HashMap<>();
        itemOfficeList.forEach(t -> {
            targetMap.put(t.getOfficeId(), t.getTitle().equals("") ? t.getGroupName() : t.getTitle());
        });
        return targetMap;
    }


}

package com.sunchs.lyt.item.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.sunchs.lyt.db.business.entity.*;
import com.sunchs.lyt.db.business.service.impl.*;
import com.sunchs.lyt.framework.bean.PagingList;
import com.sunchs.lyt.framework.util.*;
import com.sunchs.lyt.item.bean.*;
import com.sunchs.lyt.item.enums.ItemStatusEnum;
import com.sunchs.lyt.item.enums.OfficeTypeEnum;
import com.sunchs.lyt.item.exception.ItemException;
import com.sunchs.lyt.item.service.IItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.misc.BASE64Decoder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.util.*;

@Service
public class ItemService implements IItemService {

    @Autowired
    private ItemServiceImpl itemService;

    @Autowired
    private ItemOfficeServiceImpl itemOfficeService;

    @Autowired
    private HospitalOfficeServiceImpl hospitalOfficeService;

    @Autowired
    private QuestionnaireServiceImpl questionnaireService;

    @Autowired
    private AnswerServiceImpl answerService;

    @Autowired
    private HospitalServiceImpl hospitalService;

    @Autowired
    private ItemUserServiceImpl itemUserService;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private AnswerImageServiceImpl answerImageService;

    @Autowired
    private AnswerOptionServiceImpl answerOptionService;

    @Autowired
    private QuestionnaireExtendServiceImpl questionnaireExtendService;

    @Autowired
    private QuestionServiceImpl questionService;

    @Autowired
    private QuestionOptionServiceImpl questionOptionService;

    @Override
    public PagingList<ItemData> getPageList(ItemParam param) {
        List<ItemData> list = new ArrayList<>();
        Wrapper<Item> w = new EntityWrapper<>();
        w.orderBy(Item.ID, false);
        Page<Item> page = itemService.selectPage(new Page<>(param.getPageNow(), param.getPageSize()), w);
        page.getRecords().forEach(row -> list.add(getItemInfo(row)));
        return PagingUtil.getData(list, page.getSize(), param.getPageNow(), param.getPageSize());
    }

    @Override
    public int save(ItemParam param) {
        if (param.getId() == 0) {
            param.filterParam();
            Wrapper<Item> w = new EntityWrapper<>();
            w.eq(Item.NUMBER, param.getNumber());
            int count = itemService.selectCount(w);
            if (count > 0) {
                throw new ItemException("项目编号已存在");
            }
            Wrapper<Item> wTit = new EntityWrapper<>();
            wTit.eq(Item.TITLE, param.getTitle());
            int titCount = itemService.selectCount(wTit);
            if (titCount > 0) {
                throw new ItemException("项目标题已存在");
            }
        }

        Item data = new Item();
        data.setId(param.getId());
        // 修改时，不修改
        if (NumberUtil.isZero(param.getId())) {
            data.setHospitalId(param.getHospitalId());
            data.setNumber(param.getNumber());
            data.setTitle(param.getTitle());
            data.setCheckType(param.getCheckType());
            data.setIsBatch(param.getIsBatch());
            data.setStartTime(FormatUtil.dateTime(param.getStartTime()));
            data.setEndTime(FormatUtil.dateTime(param.getEndTime()));
            data.setCheckQty(param.getCheckQty());
        }
        data.setNowQty(param.getNowQty());
        if (StringUtil.isNotEmpty(param.getApproachTime())) {
            data.setApproachTime(FormatUtil.dateTime(param.getApproachTime()));
        }
        if (StringUtil.isNotEmpty(param.getDeliveryTime())) {
            data.setDeliveryTime(FormatUtil.dateTime(param.getDeliveryTime()));
        }
        if (StringUtil.isNotEmpty(param.getDataAnalysisTime())) {
            data.setDataAnalysisTime(FormatUtil.dateTime(param.getDataAnalysisTime()));
        }
        if (StringUtil.isNotEmpty(param.getReportStartTime())) {
            data.setReportStartTime(FormatUtil.dateTime(param.getReportStartTime()));
        }
        if (StringUtil.isNotEmpty(param.getReportEndTime())) {
            data.setReportEndTime(FormatUtil.dateTime(param.getReportEndTime()));
        }
        data.setUserId(param.getUserId());
        data.setUpdateId(UserThreadUtil.getUserId());
        data.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        if (NumberUtil.isZero(param.getId())) {
            data.setCreateId(UserThreadUtil.getUserId());
            data.setCreateTime(new Timestamp(System.currentTimeMillis()));
        }
        return itemService.insertOrUpdate(data) ? data.getId() : 0;
    }

    @Override
    public int bindOfficeQuestionnaire(BindOfficeParam param) {
//        // 清理历史数据
//        Wrapper<ItemOffice> w = new EntityWrapper<>();
//        w.eq(ItemOffice.ITEM_ID, param.getItemId());
//        itemOfficeService.delete(w);
        // 添加新数据
        param.getBindList().forEach(row -> {
            if (Objects.nonNull(row) && row.getQuestionnaireId() > 0) {
                if (row.getOfficeTypeId() == 3) {
                    Wrapper<ItemOffice> w = new EntityWrapper<>();
                    w.eq(ItemOffice.ITEM_ID, param.getItemId());
                    w.eq(ItemOffice.OFFICE_TYPE_ID, 3);
                    itemOfficeService.delete(w);

                    ItemOffice data = new ItemOffice();
                    data.setItemId(param.getItemId());
                    data.setHospitalId(getHospitalId(param.getItemId()));
                    data.setOfficeTypeId(row.getOfficeTypeId());
                    data.setOfficeId(0);
                    data.setQuestionnaireId(row.getQuestionnaireId());
                    itemOfficeService.insert(data);
                } else {
                    row.getOfficeList().forEach(officeId -> {
                        ItemOffice data = new ItemOffice();
                        data.setItemId(param.getItemId());
                        data.setHospitalId(getHospitalId(param.getItemId()));
                        data.setOfficeTypeId(row.getOfficeTypeId());
                        data.setOfficeId(officeId);
                        data.setQuestionnaireId(row.getQuestionnaireId());
                        // 附带科室详情
                        HospitalOffice hospitalOffice = getHospitalOfficeById(officeId);
                        if (Objects.nonNull(hospitalOffice)) {
                            data.setTitle(hospitalOffice.getTitle());
                            data.setQuantity(hospitalOffice.getQuantity());
                        }
                        itemOfficeService.insert(data);
                    });
                }
            }
        });
        return param.getItemId();
    }

    @Override
    public ItemData getById(int itemId) {
        Item item = itemService.selectById(itemId);
        if (Objects.isNull(item)) {
            return null;
        }
        ItemData data = ObjectUtil.copy(item, ItemData.class);
        data.setStartTimeName(FormatUtil.dateTime(data.getStartTime()));
        data.setEndTimeName(FormatUtil.dateTime(data.getEndTime()));
        data.setApproachTimeName(FormatUtil.dateTime(data.getApproachTime()));
        data.setDeliveryTimeName(FormatUtil.dateTime(data.getDeliveryTime()));
        data.setDataAnalysisTimeName(FormatUtil.dateTime(data.getDataAnalysisTime()));
        data.setReportStartTimeName(FormatUtil.dateTime(data.getReportStartTime()));
        data.setReportEndTimeName(FormatUtil.dateTime(data.getReportEndTime()));
        return data;
    }

    @Override
    public List<Map<String, Object>> getOfficeList(ItemParam param) {
        List<Map<String, Object>> list = new ArrayList<>();
        Wrapper<ItemOffice> wrapper = new EntityWrapper<ItemOffice>()
                .eq(ItemOffice.ITEM_ID, param.getId());
        itemOfficeService.selectList(wrapper).forEach(o -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", o.getId());
            map.put("officeTypeId", o.getOfficeTypeId());
            map.put("officeId", o.getOfficeId());
            map.put("questionnaireId", o.getQuestionnaireId());
            map.put("title", o.getTitle());
            map.put("quantity", o.getQuantity());
            list.add(map);
        });
        return list;
    }

    @Override
    public PagingList<ItemOfficeData> getOfficePageList(ItemParam param) {
        List<ItemOfficeData> list = new ArrayList<>();
        Wrapper<ItemOffice> wrapper = new EntityWrapper<>();
        if (param.getId() > 0) {
            wrapper.eq(ItemOffice.ITEM_ID, param.getId());
        }
        wrapper.orderBy(ItemOffice.OFFICE_TYPE_ID, true);
        Page<ItemOffice> page = itemOfficeService.selectPage(new Page<>(param.getPageNow(), param.getPageSize()), wrapper);
        page.getRecords().forEach(row -> {
            ItemOfficeData data = ObjectUtil.copy(row, ItemOfficeData.class);

            Item item = itemService.selectById(row.getItemId());
            if (Objects.nonNull(item)) {
                data.setHospitalName(getHospitalNameById(item.getHospitalId()));
            }

            int answerQuantity = getAnswerQuantity(row.getItemId(), row.getOfficeId(), row.getQuestionnaireId());
            data.setAnswerQuantity(answerQuantity);
            String inputItemName = getInputItemName(row.getItemId(), row.getOfficeId(), row.getQuestionnaireId());
            data.setInputTimeName(inputItemName);
            data.setOfficeName(getOfficeNameById(row.getOfficeId()));
            data.setQuestionnaireName(getQuestionnaireNameById(row.getQuestionnaireId()));
            data.setOfficeTypeName(OfficeTypeEnum.get(row.getOfficeTypeId()));
            list.add(data);
        });
        return PagingUtil.getData(list, page.getTotal(), page.getCurrent(), page.getSize());
    }

    @Override
    public void updateStatus(ItemParam param) {
        Item data = new Item();
        data.setId(param.getId());
        data.setStatus(param.getStatus());
        data.setUpdateId(UserThreadUtil.getUserId());
        data.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        itemService.updateById(data);
    }

    @Override
    public List<Map<String, Object>> getOfficePlan(int itemId, int officeTypeId) {
        List<Map<String, Object>> list = new ArrayList<>();
        Wrapper<ItemOffice> wrapper = new EntityWrapper<ItemOffice>()
                .eq(ItemOffice.ITEM_ID, itemId)
                .eq(ItemOffice.OFFICE_TYPE_ID, officeTypeId);
        itemOfficeService.selectList(wrapper).forEach(o -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", o.getId());
            map.put("officeTypeId", o.getOfficeTypeId());
            map.put("title", o.getTitle());
            map.put("quantity", o.getQuantity());
            // 已审核的数量
            Wrapper<Answer> w = new EntityWrapper<Answer>()
                    .eq(Answer.ITEM_ID, itemId)
                    .eq(Answer.OFFICE_ID, o.getOfficeId())
                    .eq(Answer.STATUS, 1);
            int count = answerService.selectCount(w);
            map.put("answerQuantity", count);
            list.add(map);
        });
        return list;
    }

    @Override
    public void updateOfficeQuantity(OfficeQuantityParam param) {
        param.getList().forEach(o -> {
            ItemOffice data = new ItemOffice();
            data.setId(o.getId());
            data.setQuantity(o.getQuantity());
            itemOfficeService.updateById(data);
        });
    }



    private List<Integer> getHospitalOffice(BindOfficeParam param) {
        Wrapper<Item> w = new EntityWrapper<>();
        w.eq(Item.ID, param.getItemId());
        Item item = itemService.selectOne(w);
        if (Objects.isNull(item)) {
            throw new ItemException("项目不存在，ID：" + param.getItemId());
        }

        List<Integer> ids = new ArrayList<>();

        Wrapper<HospitalOffice> hoWhere = new EntityWrapper<>();
        hoWhere.eq(HospitalOffice.HOSPITAL_ID, item.getHospitalId());
        List<HospitalOffice> officeList = hospitalOfficeService.selectList(hoWhere);
        officeList.forEach(office -> ids.add(office.getId()));
        return ids;
    }

    @Override
    public void unbindItemOffice(int id) {
        Wrapper<ItemOffice> w = new EntityWrapper<>();
        w.eq(ItemOffice.ID, id);
        itemOfficeService.delete(w);
    }

    @Override
    public void updateItemOfficeQuantity(int id, int quantity) {
        ItemOffice data = new ItemOffice();
        data.setId(id);
        data.setQuantity(quantity);
        itemOfficeService.updateById(data);
    }

    @Override
    public void addItemUser(int itemId, int userId) {
        ItemUser data = new ItemUser();
        data.setItemId(itemId);
        data.setUserId(userId);
        itemUserService.insert(data);
    }

    @Override
    public void removeItemUser(int itemId, int userId) {
        Wrapper<ItemUser> wrapper = new EntityWrapper<ItemUser>()
                .eq(ItemUser.ITEM_ID, itemId)
                .eq(ItemUser.USER_ID, userId);
        itemUserService.delete(wrapper);
    }

    @Override
    public List<User> itemUserList(ItemParam param) {
        List<Integer> ids = new ArrayList<>();
        Wrapper<ItemUser> wrapper = new EntityWrapper<ItemUser>()
                .eq(ItemUser.ITEM_ID, param.getId());
        itemUserService.selectList(wrapper).forEach(u -> {
            ids.add(u.getUserId());
        });
        if (ids.size() == 0) {
            return new ArrayList<>();
        }
        Wrapper<User> w = new EntityWrapper<User>()
                .in(User.ID, ids);
        return userService.selectList(w);
    }

    @Override
    public void syncAnswer(SyncAnswerParam param) {
        // 检查重复
        checkAnswer(param);

        // 获取项目相关信息
        ItemOffice itemOffice = getItemOffice(param.getItemId(), param.getOfficeId());

        // 插入答卷
        Answer data = new Answer();
        data.setHospitalId(itemOffice.getHospitalId());
        data.setItemId(param.getItemId());
        data.setOfficeId(param.getOfficeId());
        data.setQuestionnaireId(itemOffice.getQuestionnaireId());
        data.setUserId(UserThreadUtil.getUserId());
        data.setPatientNumber(param.getPatientNumber());
        data.setStatus(0);
        data.setReason("");
        data.setTimeDuration(param.getTimeDuration());
        data.setStartTime(FormatUtil.dateTime(param.getStartTime()));
        data.setEndTime(FormatUtil.dateTime(param.getEndTime()));
        data.setUpdateId(UserThreadUtil.getUserId());
        data.setUpdateTime(new Date());
        data.setCreateId(UserThreadUtil.getUserId());
        data.setCreateTime(new Date());
        if (answerService.insert(data)) {
            // 插入图片
            param.getImageList().forEach(img -> {
                if (StringUtil.isNotEmpty(img.getPath())) {
                    AnswerImage answerImage = new AnswerImage();
                    answerImage.setAnswerId(data.getId());
                    answerImage.setPath(img.getPath());
                    answerImageService.insert(answerImage);
                } else if (StringUtil.isNotEmpty(img.getData())) {
                    String fileSuffix = "";
                    if (img.getData().indexOf("data:image/png;base64,") != -1) {
                        fileSuffix = ".png";
                        String replace = img.getData().replace("data:image/png;base64,", "");
                        img.setData(replace);
                    } else if (img.getData().indexOf("data:image/jpg;base64,") != -1) {
                        fileSuffix = ".jpg";
                        String replace = img.getData().replace("data:image/jpg;base64,", "");
                        img.setData(replace);
                    } else if (img.getData().indexOf("data:image/jpeg;base64,") != -1) {
                        fileSuffix = ".jpg";
                        String replace = img.getData().replace("data:image/jpeg;base64,", "");
                        img.setData(replace);
                    }
                    if ( ! fileSuffix.equals("")) {
                        String basePath = "/lyt";
                        buildFolder(basePath);
                        String filePath = "/item_"+data.getItemId();
                        buildFolder(basePath + filePath);
                        // 文件路径
                        String file = filePath + "/answer_" + data.getId() + "_" + param.getImageList().indexOf(img) + fileSuffix;
                        buildFolder(file);
                        // 转换图片
                        BASE64Decoder decoder = new BASE64Decoder();
                        try {
                            // Base64解码
                            byte[] b = decoder.decodeBuffer(img.getData());
                            for (int i = 0; i < b.length; ++i) {
                                if (b[i] < 0) {// 调整异常数据
                                    b[i] += 256;
                                }
                            }
                            OutputStream out = new FileOutputStream(basePath + file);
                            out.write(b);
                            out.flush();
                            out.close();

                            AnswerImage answerImage = new AnswerImage();
                            answerImage.setAnswerId(data.getId());
                            answerImage.setPath(file);
                            answerImageService.insert(answerImage);
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                        }
                    }
                }
            });
            // 插入答题
            param.getQuestionList().forEach(q -> {
                // 检查题目
                boolean isExist = answerQuestionIsExist(data.getQuestionnaireId(), q.getQuestionId());
                if (isExist) {
                    QuestionOption option = getQuestionOption(q.getOptionId());
                    if (Objects.nonNull(option) && option.getQuestionId().equals(q.getQuestionId())) {
                        AnswerOption answerOption = new AnswerOption();
                        answerOption.setAnswerId(data.getId());
                        answerOption.setQuestionId(q.getQuestionId());
                        answerOption.setQuestionName(getQuestionNameById(q.getQuestionId()));
//                    answerOption.setQuestionName(q.getQuestionName());
                        answerOption.setOptionId(q.getOptionId());
                        answerOption.setOptionName(option.getTitle());
//                    answerOption.setOptionName(q.getOptionName());
                        answerOptionService.insert(answerOption);
                    } else {
                        System.out.println("同步参数有误");
                    }
                }
            });
        }
    }

    @Override
    public List<UserItemData> getUserItemOfficeList() {
        List<Integer> officeIds = getUserItemOfficeIds();
        List<UserItemData> list = new ArrayList<>();
        Wrapper<Item> wrapper = new EntityWrapper<Item>()
                .in(Item.ID, officeIds)
                .eq(Item.STATUS, 1);
        itemService.selectList(wrapper).forEach(item -> {
            UserItemData data = new UserItemData();
            data.setItemId(item.getId());
            data.setItemName(item.getTitle());
            data.setNumber(item.getNumber());
            data.setHospitalId(item.getHospitalId());
            data.setHospitalName(getHospitalNameById(item.getHospitalId()));
            data.setOfficeList(getUserItemOfficeList(item.getId()));
            list.add(data);
        });
        return list;
    }

    private List<UserItemOfficeData> getUserItemOfficeList(int itemId) {
        List<UserItemOfficeData> list = new ArrayList<>();
        Wrapper<ItemOffice> wrapper = new EntityWrapper<ItemOffice>()
                .eq(ItemOffice.ITEM_ID, itemId);
        itemOfficeService.selectList(wrapper).forEach(office -> {
            UserItemOfficeData data = new UserItemOfficeData();
            data.setOfficeTypeId(office.getOfficeTypeId());
            data.setOfficeTypeName(OfficeTypeEnum.get(office.getOfficeTypeId()));
            data.setOfficeId(office.getOfficeId());
            data.setOfficeName(getOfficeNameById(office.getOfficeId()));
            data.setQuestionnaireId(office.getQuestionnaireId());
            data.setQuestionnaireName(getQuestionnaireNameById(office.getQuestionnaireId()));
            list.add(data);
        });
        return list;
    }

    private String buildFolder(String path) {
        File file = new File(path);
        if (!file.exists() && !file.isDirectory()) {
            file.mkdir();
        }
        return path;
    }

    private ItemOffice getItemOffice(int itemId, int officeId) {
        Wrapper<ItemOffice> wrapper = new EntityWrapper<ItemOffice>()
                .eq(ItemOffice.ITEM_ID, itemId)
                .eq(ItemOffice.OFFICE_ID, officeId);
        return itemOfficeService.selectOne(wrapper);
    }

    private void checkAnswer(SyncAnswerParam param) {
        Wrapper<Answer> wrapper = new EntityWrapper<Answer>()
                .eq(Answer.ITEM_ID, param.getItemId())
                .eq(Answer.OFFICE_ID, param.getOfficeId())
                .eq(Answer.PATIENT_NUMBER, param.getPatientNumber());
        int count = answerService.selectCount(wrapper);
        if (count > 0) {
            throw new ItemException("答卷无需重复上传");
        }
    }

    private ItemData getItemInfo(Item item) {
        ItemData res = ObjectUtil.copy(item, ItemData.class);

        // 获取问卷列表
        res.setQuestionnaireList(getQuestionnaireList(item));

        // 项目总进度
        List<OfficePlanData> planList = new ArrayList<>();
        Wrapper<ItemOffice> wrapper = new EntityWrapper<ItemOffice>()
                .eq(ItemOffice.ITEM_ID, item.getId())
                .groupBy(ItemOffice.OFFICE_TYPE_ID);
        itemOfficeService.selectList(wrapper).forEach(type -> {
            OfficePlanData oData = new OfficePlanData();
            oData.setOfficeTypeId(type.getOfficeTypeId());
            switch (type.getOfficeTypeId()) {
                case 1:
                    oData.setOfficeTypeName("门诊");
                    break;
                case 2:
                    oData.setOfficeTypeName("住院");
                    break;
                case 3:
                    oData.setOfficeTypeName("员工");
                    break;
                case 4:
                    oData.setOfficeTypeName("特殊");
                    break;
                default:
                    break;
            }

            int answerQty = 0;
            Wrapper<ItemOffice> w = new EntityWrapper<ItemOffice>()
                    .eq(ItemOffice.ITEM_ID, item.getId())
                    .eq(ItemOffice.OFFICE_TYPE_ID, type.getOfficeTypeId());
            List<ItemOffice> officeList = itemOfficeService.selectList(w);
            for (ItemOffice office : officeList) {
                Wrapper<Answer> answerWrapper = new EntityWrapper<Answer>()
                        .eq(Answer.ITEM_ID, item.getId())
                        .eq(Answer.OFFICE_ID, office.getOfficeId())
                        .eq(Answer.STATUS, 1);
                int count = answerService.selectCount(answerWrapper);
                answerQty += count;
            }

            oData.setQuantity(answerQty);
            planList.add(oData);
        });
        res.setOfficePlanList(planList);

        // 状态
        res.setStatusName(ItemStatusEnum.get(res.getStatus()));

        // 完成时间
        res.setFinishTimeName(FormatUtil.dateTime(res.getEndTime()));

        // 创建时间
        res.setCreateTimeName(FormatUtil.dateTime(res.getCreateTime()));

        return res;
    }

    private HospitalOffice getHospitalOfficeById(int officeId) {
        Wrapper<HospitalOffice> wrapper = new EntityWrapper<HospitalOffice>()
                .eq(HospitalOffice.ID, officeId);
        return hospitalOfficeService.selectOne(wrapper);
    }

    private List<Questionnaire> getQuestionnaireList(Item item) {
        Set<Integer> ids = getQuestionnaireIds(item);
        if (ids.size() == 0) {
            return new ArrayList<>();
        }
        Wrapper<Questionnaire> wrapper = new EntityWrapper<Questionnaire>()
                .in(Questionnaire.ID, ids);
        return questionnaireService.selectList(wrapper);
    }

    private Set<Integer> getQuestionnaireIds(Item item) {
        Set<Integer> ids = new HashSet<>();
        Wrapper<ItemOffice> wrapper = new EntityWrapper<ItemOffice>()
                .eq(ItemOffice.ITEM_ID, item.getId());
        itemOfficeService.selectList(wrapper).forEach(q -> {
            ids.add(q.getQuestionnaireId());
        });
        return ids;
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

    private int getAnswerQuantity(int itemId, int officeId, int questionnaireId) {
        Wrapper<Answer> wrapper = new EntityWrapper<Answer>()
                .eq(Answer.ITEM_ID, itemId)
                .eq(Answer.OFFICE_ID, officeId)
                .eq(Answer.QUESTIONNAIRE_ID, questionnaireId);
        return answerService.selectCount(wrapper);
    }

    private String getInputItemName(int itemId, int officeId, int questionnaireId) {
        Wrapper<Answer> wrapper = new EntityWrapper<Answer>()
                .eq(Answer.ITEM_ID, itemId)
                .eq(Answer.OFFICE_ID, officeId)
                .eq(Answer.QUESTIONNAIRE_ID, questionnaireId)
                .orderBy(Answer.ID, false);
        Answer answer = answerService.selectOne(wrapper);
        if (Objects.nonNull(answer)) {
            return FormatUtil.dateTime(answer.getCreateTime());
        }
        return "无";
    }

    private int getHospitalId(int itemId) {
        Wrapper<Item> wrapper = new EntityWrapper<Item>()
                .eq(Item.ID, itemId);
        Item item = itemService.selectOne(wrapper);
        if (Objects.nonNull(item)) {
            return item.getHospitalId();
        } else {
            throw new ItemException("项目不存在：" + itemId);
        }
    }

    private boolean answerQuestionIsExist(int questionnaireId, int questionId) {
        Wrapper<QuestionnaireExtend> wrapper = new EntityWrapper<QuestionnaireExtend>()
                .eq(QuestionnaireExtend.QUESTIONNAIRE_ID, questionnaireId)
                .eq(QuestionnaireExtend.QUESTION_ID, questionId);
        int count = questionnaireExtendService.selectCount(wrapper);
        return (count > 0);
    }

    private String getQuestionNameById(int questionId) {
        Wrapper<Question> wrapper = new EntityWrapper<Question>()
                .eq(Question.ID, questionId);
        Question row = questionService.selectOne(wrapper);
        if (Objects.nonNull(row)) {
            return row.getTitle();
        }
        return "";
    }

    private QuestionOption getQuestionOption(int optionId) {
        Wrapper<QuestionOption> wrapper = new EntityWrapper<QuestionOption>()
                .eq(QuestionOption.ID, optionId);
        return questionOptionService.selectOne(wrapper);
    }

    private List<Integer> getUserItemOfficeIds() {
        List<Integer> ids = new ArrayList<>();
        Wrapper<ItemUser> wrapper = new EntityWrapper<ItemUser>()
                .eq(ItemUser.USER_ID, UserThreadUtil.getUserId());
        itemUserService.selectList(wrapper).forEach(o -> ids.add(o.getItemId()));
        return ids;
    }
}

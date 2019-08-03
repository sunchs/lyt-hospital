package com.sunchs.lyt.item.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.sunchs.lyt.db.business.entity.Answer;
import com.sunchs.lyt.db.business.entity.AnswerImage;
import com.sunchs.lyt.db.business.entity.AnswerOption;
import com.sunchs.lyt.db.business.entity.Item;
import com.sunchs.lyt.db.business.service.impl.AnswerImageServiceImpl;
import com.sunchs.lyt.db.business.service.impl.AnswerOptionServiceImpl;
import com.sunchs.lyt.db.business.service.impl.AnswerServiceImpl;
import com.sunchs.lyt.db.business.service.impl.ItemServiceImpl;
import com.sunchs.lyt.framework.bean.PagingList;
import com.sunchs.lyt.framework.util.FormatUtil;
import com.sunchs.lyt.framework.util.PagingUtil;
import com.sunchs.lyt.framework.util.StringUtil;
import com.sunchs.lyt.framework.util.UserThreadUtil;
import com.sunchs.lyt.item.bean.AnswerData;
import com.sunchs.lyt.item.bean.AnswerImageData;
import com.sunchs.lyt.item.bean.AnswerParam;
import com.sunchs.lyt.item.enums.AnswerStatusEnum;
import com.sunchs.lyt.item.service.IAnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class AnswerService implements IAnswerService {

    @Autowired
    private AnswerServiceImpl answerService;

    @Autowired
    private AnswerImageServiceImpl answerImageService;

    @Autowired
    private AnswerOptionServiceImpl answerOptionService;

    @Autowired
    private ItemServiceImpl itemService;

    @Override
    public PagingList<AnswerData> getPageList(AnswerParam param) {
        Wrapper<Answer> wrapper = new EntityWrapper<>();
        if (param.getItemId() > 0) {
            wrapper.eq(Answer.ITEM_ID, param.getItemId());
        }
        if (param.getOfficeId() > 0) {
            wrapper.eq(Answer.OFFICE_ID, param.getOfficeId());
        }
        Page<Answer> data = answerService.selectPage(new Page<>(param.getPageNow(), param.getPageSize()), wrapper);
        List<AnswerData> list = new ArrayList<>();
        data.getRecords().forEach(answer -> list.add(toAnswerData(answer)));
        return PagingUtil.getData(list, data.getTotal(), data.getCurrent(), data.getSize());
    }

    @Override
    public int save(AnswerParam param) {
//        data.setUpdateId(UserThreadUtil.getUserId());
//        data.setUpdateTime(new Timestamp(System.currentTimeMillis()));
//        data.setCreateId(UserThreadUtil.getUserId());
//        data.setCreateTime(new Timestamp(System.currentTimeMillis()));

        return 0;
    }

    @Override
    public void updateStatus(AnswerParam param) {
        if (param.getId() == 0) {
            param.getIdList().forEach(id -> {
                Answer data = new Answer();
                data.setId(id);
                data.setStatus(param.getStatus());
                if (StringUtil.isNotEmpty(param.getReason())) {
                    data.setReason(param.getReason());
                }
                data.setUpdateId(UserThreadUtil.getUserId());
                data.setUpdateTime(new Timestamp(System.currentTimeMillis()));
                answerService.updateById(data);
            });
        } else {
            Answer data = new Answer();
            data.setId(param.getId());
            data.setStatus(param.getStatus());
            if (StringUtil.isNotEmpty(param.getReason())) {
                data.setReason(param.getReason());
            }
            data.setUpdateId(UserThreadUtil.getUserId());
            data.setUpdateTime(new Timestamp(System.currentTimeMillis()));
            answerService.updateById(data);
        }
    }

    @Override
    public void updateReason(AnswerParam param) {
        Answer data = new Answer();
        data.setId(param.getId());
        data.setReason(param.getReason());
        data.setUpdateId(UserThreadUtil.getUserId());
        data.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        answerService.updateById(data);
    }

    private AnswerData toAnswerData(Answer answer) {
        AnswerData data = new AnswerData();
        data.setId(answer.getId());
        data.setHospitalId(answer.getHospitalId());
        data.setItemId(answer.getItemId());
        data.setOfficeId(answer.getOfficeId());
        data.setQuestionnaireId(answer.getQuestionnaireId());
        data.setUserId(answer.getUserId());
        data.setPatientNumber(answer.getPatientNumber());
        data.setStatus(answer.getStatus());
        data.setReason(answer.getReason());
        data.setTimeDuration(answer.getTimeDuration());
        data.setStartTime(answer.getStartTime());
        data.setEndTime(answer.getEndTime());
        data.setCreateTime(answer.getCreateTime());

        data.setItemName(getItemName(answer.getItemId()));
        data.setStatusName(AnswerStatusEnum.get(answer.getStatus()));
        data.setStartTimeName(FormatUtil.dateTime(answer.getStartTime()));
        data.setEndTimeName(FormatUtil.dateTime(answer.getEndTime()));
        data.setCreateTimeName(FormatUtil.dateTime(answer.getCreateTime()));

        data.setQuestionOptionList(getQuestionOptionList(answer.getId()));
        data.setImageList(getImageList(answer.getId()));

        return data;
    }

    /**
     * 获取答卷图片
     */
    private List<AnswerImageData> getImageList(int answerId) {
        Wrapper<AnswerImage> wrapper = new EntityWrapper<AnswerImage>()
                .eq(AnswerImage.ANSWER_ID, answerId);
        List<AnswerImageData> imgList = new ArrayList<>();
        answerImageService.selectList(wrapper).forEach(img -> {
            AnswerImageData imageData = new AnswerImageData();
            imageData.setPath(img.getPath());
            imgList.add(imageData);
        });
        return imgList;
    }

    /**
     * 获取答卷题目选项
     */
    private List<AnswerOption> getQuestionOptionList(int answerId) {
        Wrapper<AnswerOption> wrapper = new EntityWrapper<AnswerOption>()
                .eq(AnswerOption.ANSWER_ID, answerId);
        return answerOptionService.selectList(wrapper);
    }

    /**
     * 获取项目名称
     */
    private String getItemName(int itemId) {
        Wrapper<Item> wrapper = new EntityWrapper<Item>()
                .eq(Item.ID, itemId);
        Item item = itemService.selectOne(wrapper);
        if (Objects.nonNull(item)) {
            return item.getTitle();
        }
        return "";
    }
}

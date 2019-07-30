package com.sunchs.lyt.item.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.sunchs.lyt.db.business.entity.Answer;
import com.sunchs.lyt.db.business.entity.AnswerImage;
import com.sunchs.lyt.db.business.service.impl.AnswerImageServiceImpl;
import com.sunchs.lyt.db.business.service.impl.AnswerServiceImpl;
import com.sunchs.lyt.framework.bean.PagingList;
import com.sunchs.lyt.framework.util.FormatUtil;
import com.sunchs.lyt.framework.util.PagingUtil;
import com.sunchs.lyt.item.bean.AnswerData;
import com.sunchs.lyt.item.bean.AnswerImageData;
import com.sunchs.lyt.item.bean.AnswerParam;
import com.sunchs.lyt.item.enums.AnswerStatusEnum;
import com.sunchs.lyt.item.service.IAnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AnswerService implements IAnswerService {

    @Autowired
    private AnswerServiceImpl answerService;

    @Autowired
    private AnswerImageServiceImpl answerImageService;

    @Override
    public PagingList<AnswerData> getPageList(AnswerParam param) {
        Wrapper<Answer> wrapper = new EntityWrapper<>();
        Page<Answer> data = answerService.selectPage(new Page<>(param.getPageNow(), param.getPageSize()), wrapper);
        List<AnswerData> list = new ArrayList<>();
        data.getRecords().forEach(answer -> list.add(toAnswerData(answer)));
        return PagingUtil.getData(list, data.getTotal(), data.getCurrent(), data.getSize());
    }

    @Override
    public int save(AnswerParam param) {
        return 0;
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

        data.setStatusName(AnswerStatusEnum.get(answer.getStatus()));
        data.setStartTimeName(FormatUtil.dateTime(answer.getStartTime()));
        data.setEndTimeName(FormatUtil.dateTime(answer.getEndTime()));
        data.setCreateTimeName(FormatUtil.dateTime(answer.getCreateTime()));


        Wrapper<AnswerImage> wrapper = new EntityWrapper<AnswerImage>()
                .eq(AnswerImage.ANSWER_ID, answer.getId());
        List<AnswerImageData> imgList = new ArrayList<>();
        answerImageService.selectList(wrapper).forEach(img -> {
            AnswerImageData imageData = new AnswerImageData();
            imageData.setPath(img.getPath());
            imgList.add(imageData);
        });
        data.setImageList(imgList);

        return data;
    }
}

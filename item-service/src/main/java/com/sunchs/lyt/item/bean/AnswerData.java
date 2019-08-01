package com.sunchs.lyt.item.bean;

import com.sunchs.lyt.db.business.entity.Answer;
import com.sunchs.lyt.db.business.entity.AnswerOption;

import java.util.List;

public class AnswerData extends Answer {

    private String statusName;

    private String createTimeName;

    private String startTimeName;

    private String endTimeName;

    private List<AnswerOption> questionOptionList;

    private List<AnswerImageData> imageList;

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getCreateTimeName() {
        return createTimeName;
    }

    public void setCreateTimeName(String createTimeName) {
        this.createTimeName = createTimeName;
    }

    public String getStartTimeName() {
        return startTimeName;
    }

    public void setStartTimeName(String startTimeName) {
        this.startTimeName = startTimeName;
    }

    public String getEndTimeName() {
        return endTimeName;
    }

    public void setEndTimeName(String endTimeName) {
        this.endTimeName = endTimeName;
    }

    public List<AnswerOption> getQuestionOptionList() {
        return questionOptionList;
    }

    public void setQuestionOptionList(List<AnswerOption> questionOptionList) {
        this.questionOptionList = questionOptionList;
    }

    public List<AnswerImageData> getImageList() {
        return imageList;
    }

    public void setImageList(List<AnswerImageData> imageList) {
        this.imageList = imageList;
    }
}

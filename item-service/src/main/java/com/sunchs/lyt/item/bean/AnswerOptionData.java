package com.sunchs.lyt.item.bean;

import com.sunchs.lyt.db.business.entity.AnswerOption;

import java.util.List;

public class AnswerOptionData extends AnswerOption {

    private List<Integer> tagIds;

    private String reason;

    public List<Integer> getTagIds() {
        return tagIds;
    }

    public void setTagIds(List<Integer> tagIds) {
        this.tagIds = tagIds;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}

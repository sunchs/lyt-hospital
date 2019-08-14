package com.sunchs.lyt.item.bean;

import com.sunchs.lyt.db.business.entity.AnswerOption;

import java.util.List;

public class AnswerOptionData extends AnswerOption {

    private List<Integer> tagIds;

    public List<Integer> getTagIds() {
        return tagIds;
    }

    public void setTagIds(List<Integer> tagIds) {
        this.tagIds = tagIds;
    }
}

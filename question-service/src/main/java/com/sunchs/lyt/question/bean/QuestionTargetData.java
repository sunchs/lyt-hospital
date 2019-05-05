package com.sunchs.lyt.question.bean;

import com.sunchs.lyt.db.business.entity.QuestionTarget;

import java.util.List;

public class QuestionTargetData extends QuestionTarget {

    public List<QuestionTargetData> children;

    public List<QuestionTargetData> getChildren() {
        return children;
    }

    public void setChildren(List<QuestionTargetData> children) {
        this.children = children;
    }
}

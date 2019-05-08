package com.sunchs.lyt.question.bean;

import com.sunchs.lyt.db.business.entity.QuestionTarget;
import com.sunchs.lyt.framework.util.FormatUtil;

import java.util.List;

public class QuestionTargetData extends QuestionTarget {

    public String updateTimeName;

    public List<QuestionTargetData> children;

    public String getUpdateTimeName() {
        return updateTimeName;
    }

    public void setUpdateTimeName(String updateTimeName) {
        this.updateTimeName = updateTimeName;
    }

    public List<QuestionTargetData> getChildren() {
        return children;
    }

    public void setChildren(List<QuestionTargetData> children) {
        this.children = children;
    }

    public void initData() {
        setUpdateTimeName(FormatUtil.dateTime(getUpdateTime()));
    }
}

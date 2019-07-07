package com.sunchs.lyt.question.bean;

import com.sunchs.lyt.question.exception.QuestionException;
import org.springframework.util.CollectionUtils;

import java.util.List;

public class OptionTemplateParam {

    private int id;
    private int pid;
    private List<String> optionList;

    public void checkOption() {
        if (CollectionUtils.isEmpty(optionList)) {
            throw new QuestionException("选项不能为空");
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public List<String> getOptionList() {
        return optionList;
    }

    public void setOptionList(List<String> optionList) {
        this.optionList = optionList;
    }
}

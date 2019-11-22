package com.sunchs.lyt.question.bean;

import com.sunchs.lyt.question.exception.QuestionException;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

public class OptionTemplateParam {

    private int id;
    private int pid;
    private int status;
    private List<OptionTemplateOptionParam> optionList;

    public void checkPid() {
        if (pid == 0) {
            throw new QuestionException("选项类型不能为空");
        }
    }

    public void checkOption() {
        if (CollectionUtils.isEmpty(optionList)) {
            throw new QuestionException("选项内容不能为空");
        }
        for (int i = 0; i < optionList.size(); i++) {
            String nVal = optionList.get(i).getValue().replaceAll(",", "，");
            nVal.replaceAll("", " ");

            OptionTemplateOptionParam data = new OptionTemplateOptionParam();
            data.setValue(nVal);
            data.setScore(optionList.get(i).getScore());
            optionList.set(i, data);
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<OptionTemplateOptionParam> getOptionList() {
        return optionList;
    }

    public void setOptionList(List<OptionTemplateOptionParam> optionList) {
        this.optionList = optionList;
    }

    public List<String> getOptionValueList() {
        List<String> list = new ArrayList<>();
        this.optionList.forEach(o -> {
            String val = o.getValue() + "::" + o.getScore();
            list.add(val);
        });
        return list;
    }
}

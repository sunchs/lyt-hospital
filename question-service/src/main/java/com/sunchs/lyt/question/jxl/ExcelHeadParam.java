package com.sunchs.lyt.question.jxl;

import java.util.List;

public class ExcelHeadParam {

    public String key;
    public String title;
    public int columnWidth;

    public List<ExcelHeadParam> list;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getColumnWidth() {
        return columnWidth;
    }

    public void setColumnWidth(int columnWidth) {
        this.columnWidth = columnWidth;
    }

    public List<ExcelHeadParam> getList() {
        return list;
    }

    public void setList(List<ExcelHeadParam> list) {
        this.list = list;
    }
}

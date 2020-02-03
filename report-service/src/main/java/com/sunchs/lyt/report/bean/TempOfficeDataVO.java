package com.sunchs.lyt.report.bean;

import com.sunchs.lyt.framework.bean.TitleValueData;

import java.util.List;

public class TempOfficeDataVO {

    private List<TempOfficeData> list;
    private List<TitleValueData> rankingList;

    public List<TempOfficeData> getList() {
        return list;
    }

    public void setList(List<TempOfficeData> list) {
        this.list = list;
    }

    public List<TitleValueData> getRankingList() {
        return rankingList;
    }

    public void setRankingList(List<TitleValueData> rankingList) {
        this.rankingList = rankingList;
    }
}

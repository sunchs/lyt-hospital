package com.sunchs.lyt.report.bean;

import com.sunchs.lyt.framework.bean.TitleValueData;

import java.util.List;

public class CustomOfficeDataVO {

    private List<CustomOfficeData> list;
    private List<TitleValueData> rankingList;

    public List<CustomOfficeData> getList() {
        return list;
    }

    public void setList(List<CustomOfficeData> list) {
        this.list = list;
    }

    public List<TitleValueData> getRankingList() {
        return rankingList;
    }

    public void setRankingList(List<TitleValueData> rankingList) {
        this.rankingList = rankingList;
    }
}

package com.sunchs.lyt.report.bean;

import java.util.List;

public class CustomOfficeDataVO {

    private List<CustomOfficeData> list;
    private List<TitleValueDataVO> rankingList;

    public List<CustomOfficeData> getList() {
        return list;
    }

    public void setList(List<CustomOfficeData> list) {
        this.list = list;
    }

    public List<TitleValueDataVO> getRankingList() {
        return rankingList;
    }

    public void setRankingList(List<TitleValueDataVO> rankingList) {
        this.rankingList = rankingList;
    }
}

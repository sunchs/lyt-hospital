package com.sunchs.lyt.report.bean;

import java.util.List;

public class TempOfficeDataVO {

    private List<TempOfficeData> list;
    private List<TitleValueDataVO> rankingList;

    public List<TempOfficeData> getList() {
        return list;
    }

    public void setList(List<TempOfficeData> list) {
        this.list = list;
    }

    public List<TitleValueDataVO> getRankingList() {
        return rankingList;
    }

    public void setRankingList(List<TitleValueDataVO> rankingList) {
        this.rankingList = rankingList;
    }
}

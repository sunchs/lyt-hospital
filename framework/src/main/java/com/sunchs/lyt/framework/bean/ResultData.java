package com.sunchs.lyt.framework.bean;

public class ResultData {

    public Integer status;

    public String msg;

    public Object data;

    @Override
    public String toString() {
        return "ResultData{" +
                "status=" + status +
                ", msg='" + msg + '\'' +
                '}';
    }
}

package com.sunchs.lyt.framework.controller;

import com.sunchs.lyt.framework.bean.ResultData;

public class BaseController {

    public ResultData success() {
        return ResultData.getSuccess();
    }

    public ResultData success(Object src) {
        return ResultData.getSuccess(src);
    }



}

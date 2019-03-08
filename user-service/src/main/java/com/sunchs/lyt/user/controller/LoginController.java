package com.sunchs.lyt.user.controller;

import com.sunchs.lyt.framework.bean.RequestData;
import com.sunchs.lyt.framework.bean.ResultData;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class LoginController {

    @PostMapping("/setLogin")
    public ResultData login(@RequestBody RequestData data) {
        ResultData resultData = new ResultData();
        resultData.status = 1;
        resultData.msg = data.getPlatform();

        resultData.data = data.getString("name");

        return resultData;
    }

}

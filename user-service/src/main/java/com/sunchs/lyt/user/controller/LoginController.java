package com.sunchs.lyt.user.controller;

import com.sunchs.lyt.framework.bean.RequestData;
import com.sunchs.lyt.framework.bean.ResultData;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class LoginController {

    @PostMapping("/setLogin")
    public ResultData login(@RequestBody RequestData data) {

        Map<String, Object> res = new HashMap<>();
        res.put("version", data.getVersion());
        res.put("platform", data.getPlatform());
        res.put("params", data.getParams().toString());
        res.put("token", data.getToken());

        return ResultData.getSuccess(res);
    }

}

package com.sunchs.lyt.gateway.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.sunchs.lyt.framework.bean.RequestData;
import com.sunchs.lyt.framework.util.JsonUtil;
import com.sunchs.lyt.framework.util.StreamUtil;
import com.sunchs.lyt.framework.util.StringUtil;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component
public class PreFilter extends ZuulFilter {

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();

        String stream = null;
        try {
            stream = StreamUtil.getInputStream(request.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (StringUtil.isEmpty(stream)) {
            // 检查是否有参数
            System.out.println("请输入参数");
            return null;
        }
        RequestData data = JsonUtil.toObject(stream, RequestData.class);
        if (data == null) {
            // 检查参数合法性
            System.out.println("JSON语法不正确");
            return null;
        }

        if (StringUtil.isEmpty(data.getVersion())) {
            System.out.println("版本 不能为空");
            return null;
        }

        if (StringUtil.isEmpty(data.getPlatform())) {
            System.out.println("平台 不能为空");
            return null;
        }

        if (StringUtil.isEmpty(data.getToken())) {
            System.out.println("token 不能为空");
            return null;
        }

//        System.out.println("-----------pre-----> run");
//
//        requestContext.setSendZuulResponse(true);
//        requestContext.setResponseStatusCode(200);
//        requestContext.setResponseBody("{\"name\":\"chhliu\"}");// 输出最终结果
        return null;
    }
}

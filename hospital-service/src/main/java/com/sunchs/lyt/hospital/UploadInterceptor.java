package com.sunchs.lyt.hospital;

import org.springframework.util.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 允许请求跨域访问本服务器
 * 跨域上传必须！
 *
 * @author JiaBochao
 * @version 2017-11-17 15:34:32
 */
public class UploadInterceptor extends HandlerInterceptorAdapter {

    //private String webDomain;

    public UploadInterceptor(){

    }


    /**
     * 设置允许跨域的头信息
     *
     */
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) {
        //httpServletResponse.setHeader("Access-Control-Allow-Credentials", "false");
        //httpServletResponse.setHeader("Access-Control-Allow-Origin", "http://www.jthinking.com");
       /* httpServletResponse.setHeader("Access-Control-Allow-Origin", "*");
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "*");*/

        String curOrigin = httpServletRequest.getHeader("Origin");
        httpServletResponse.setHeader("Access-Control-Allow-Origin", StringUtils.isEmpty(curOrigin) ? "*" : curOrigin);
        httpServletResponse.setHeader("Access-Control-Allow-Methods",  "POST,GET,OPTIONS,DELETE");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", "X_Requested_With,Content-Type");
       // httpServletResponse.setHeader("Access-Control-Allow-Credentials", "true");

       // httpServletResponse.setHeader("Content-Security-Policy","upgrade-insecure-requests");
        httpServletResponse.setContentType("application/json");
        httpServletResponse.setCharacterEncoding("utf-8");
        return true;
    }
}

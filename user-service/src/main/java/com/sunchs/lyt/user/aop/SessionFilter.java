package com.sunchs.lyt.user.aop;

import com.sunchs.lyt.framework.request.PostServletRequest;
import com.sunchs.lyt.framework.util.StreamUtil;
import com.sunchs.lyt.framework.util.UserThreadUtil;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component
public class SessionFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String stream = StreamUtil.getInputStream(request.getInputStream());
        UserThreadUtil.initUser(stream);
        chain.doFilter(new PostServletRequest((HttpServletRequest) request, stream), response);
    }

    @Override
    public void destroy() {

    }
}

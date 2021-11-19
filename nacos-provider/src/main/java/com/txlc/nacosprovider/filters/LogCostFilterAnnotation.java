package com.txlc.nacosprovider.filters;

import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.FilterConfig;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

/**
 * 这里需要注意一点的是@WebFilter这个注解是Servlet3.0的规范，并不是Spring boot提供的。
 * 除了这个注解以外，我们还需在启动类中加另外一个注解：@ServletComponetScan，指定扫描的包。
 */
@Order(0)
@WebFilter(urlPatterns = "/*", filterName = "logFilter2")
@Component
public class LogCostFilterAnnotation implements Filter {

    private final String REQUEST_ID = "requestId";
    private final String REQUEST_ID_HEADER = "request_id";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        long start = System.currentTimeMillis();
        System.out.println("LogFilter2 Execute cost=" + (System.currentTimeMillis() - start));
        filterChain.doFilter(servletRequest,servletResponse);
    }

    @Override
    public void destroy() {

    }

}

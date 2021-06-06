package com.beta.backend.security.filter;

import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Order(2)
public class SimpleCORSFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse rsp = (HttpServletResponse) response;
        HttpServletRequest rqt = (HttpServletRequest) request;

        rsp.setHeader("Access-Control-Allow-Origin", "*");
        rsp.setHeader("Access-Control-Allow-Methods", "GET, PUT, POST, DELETE, OPTIONS");
        rsp.addHeader("Access-Control-Allow-Headers", "*");
        if (rqt.getMethod().equals(HttpMethod.OPTIONS.name())) {
            rsp.setHeader("Access-Control-Max-Age", "1728000");
            rsp.setStatus(HttpStatus.NO_CONTENT.value());
        } else {
            rsp.setHeader("Access-Control-Expose-Headers", "DNT,X-CustomHeader,Keep-Alive,User-Agent,X-Requested-With,If-Modified-Since,Cache-Control,Content-Type,Content-Range,Range,Content-Disposition");
        }
        chain.doFilter(rqt, rsp);

    }


    @Override
    public void destroy() {

    }
}
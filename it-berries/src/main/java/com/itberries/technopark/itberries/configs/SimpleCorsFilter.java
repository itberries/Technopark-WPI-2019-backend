package com.itberries.technopark.itberries.configs;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class SimpleCorsFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //response.setHeader("Access-Control-Allow-Origin", "https://itberries-frontend.now.sh, https://localhost:8080, http://localhost:8080");
        if(request.getHeader("origin") != null && request.getHeader("origin").contains("localhost")) {
            response.setHeader("Access-Control-Allow-Origin", "http://localhost:10888");
        }else {
            response.setHeader("Access-Control-Allow-Origin", "https://itberries-explority.now.sh");
        }
        //response.setHeader("Access-Control-Allow-Origin", "https://itberries-frontend.now.sh");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, PATCH, OPTIONS");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "authorization, content-type, xsrf-token");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.addHeader("Access-Control-Expose-Headers", "xsrf-token");
        if ("OPTIONS".equals(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            filterChain.doFilter(request, response);
        }
    }
}

package com.moyoprototype.jwt.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@Component
public class ServerLoadBalancingLoggingFilter extends OncePerRequestFilter {
    private String currentServerId= UUID.randomUUID().toString();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("현재 요청이 지나는 서버 : {}", currentServerId);
        filterChain.doFilter(request,response);
    }
}

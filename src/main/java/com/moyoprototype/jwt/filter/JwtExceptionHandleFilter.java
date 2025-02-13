package com.moyoprototype.jwt.filter;

import com.moyoprototype.jwt.exception.JwtAccessExpiredException;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
@Order(1)
public class JwtExceptionHandleFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try {
            filterChain.doFilter(request, response);
        } catch (JwtAccessExpiredException e) {
            log.error("JWT Access 만료 예외 발생 ");

            response.setStatus(401);
            response.setContentType("application/json");
            String errorResponse = "{\"code\": \"LOGIN_401_1\"}";
            response.getWriter().write(errorResponse);
        }
    }


}


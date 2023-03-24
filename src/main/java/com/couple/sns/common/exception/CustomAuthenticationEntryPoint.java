package com.couple.sns.common.exception;

import com.couple.sns.common.responce.Response;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        log.error("가입되지 않은 사용자 접근 : {} ", request.getRequestURI());

        String responseBody = objectMapper.writeValueAsString(Response.error(ErrorCode.USER_NOT_FOUND.name()));
        response.setContentType("application/json");
        response.setStatus(ErrorCode.USER_NOT_FOUND.getStatus().value());
        response.getWriter().write(responseBody);
    }
}


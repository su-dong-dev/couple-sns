package com.couple.sns.common.exception;

import com.couple.sns.common.responce.Response;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        log.error("권한없는 사용자 접근 : {} ", request.getRequestURI());

        String responseBody = objectMapper.writeValueAsString(Response.error(ErrorCode.INVALID_PERMISSION.name()));
        response.setContentType("application/json");
        response.setStatus(ErrorCode.INVALID_PERMISSION.getStatus().value());
        response.getWriter().write(responseBody);
    }

}
package com.couple.sns.common.configuration.filter;

import com.couple.sns.common.configuration.util.JwtTokenUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

    private final String key;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if(header == null || !header.startsWith("Bearer ")) {
            log.warn("Error occurs while getting header. header is null or invalid.");
            filterChain.doFilter(request, response);
            return;
        }

        try {
            final String token = header.split(" ")[1].trim();

            if(JwtTokenUtils.isExpired(token, key)) {
                log.error("Key is expired");
                filterChain.doFilter(request, response);
                return;
            }

            String username = JwtTokenUtils.getUsername(token, key);
            String role = JwtTokenUtils.getUserRole(token, key);

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            username, null, Collections.singleton(new SimpleGrantedAuthority(role))
                    );

            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }catch (RuntimeException e) {
            log.error("Error occurs while validating. {}", e.toString());
            filterChain.doFilter(request, response);
            return;
        }

        filterChain.doFilter(request, response);
    }
}
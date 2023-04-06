package com.couple.sns.common.configuration;

import com.couple.sns.common.configuration.filter.JwtTokenFilter;
import com.couple.sns.common.exception.CustomAccessDeniedHandler;
import com.couple.sns.common.exception.CustomAuthenticationEntryPoint;
import com.couple.sns.common.property.JwtProperties;
import com.couple.sns.domain.user.dto.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class AuthenticationConfig {

    private final JwtProperties jwtProperties;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers(
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/configuration/ui",
            "/swagger-resources",
            "/swagger-ui",
            "/swagger-ui/**",
            "/configuration/security",
            "/swagger-ui.html",
            "/webjars/**",
        "/service-worker-noti.js");
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .cors().and().csrf().disable()
                .authorizeHttpRequests()
                .requestMatchers("/api/*/posts/*").hasAnyAuthority(UserRole.USER.name(), UserRole.ADMIN.name())
                .requestMatchers("/api/*/users/join", "/api/*/users/login").permitAll()
                .requestMatchers("/api/**").authenticated()
                .and()
                .formLogin().disable()
                .logout()
                .logoutSuccessUrl("/")
                .and()
                .addFilterBefore(new JwtTokenFilter(jwtProperties.getSecretKey()), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling()
                    .accessDeniedHandler(customAccessDeniedHandler)
                    .authenticationEntryPoint(customAuthenticationEntryPoint)
                .and()
                .build();
    }

}

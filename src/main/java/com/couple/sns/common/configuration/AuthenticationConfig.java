package com.couple.sns.common.configuration;

import com.couple.sns.common.configuration.filter.JwtTokenFilter;
import com.couple.sns.common.exception.CustomAccessDeniedHandler;
import com.couple.sns.common.exception.CustomAuthenticationEntryPoint;
import com.couple.sns.common.property.JwtProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class AuthenticationConfig {

    private final JwtProperties jwtProperties;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .cors().and().csrf().disable()
                .authorizeHttpRequests()
                .requestMatchers("/api/*/users/join", "/api/*/users/login").permitAll()
                .requestMatchers("/api/**").authenticated()
                .and()
                .formLogin().disable()
                .logout()
                .logoutSuccessUrl("/")
                .and()
                .addFilterBefore(new JwtTokenFilter(jwtProperties.getSecretKey()), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling()
                    .accessDeniedHandler(new CustomAccessDeniedHandler())
                    .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                .and()
                .build();
    }

}

package com.couple.sns.common.configuration;

import com.couple.sns.common.configuration.filter.JwtTokenFilter;
import com.couple.sns.common.property.JwtProperties;
import com.couple.sns.domain.user.service.UserUpdateService;
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

    private final UserUpdateService userUpdateService;
    private final JwtProperties jwtProperties;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .cors().and().csrf().disable()
                .authorizeHttpRequests()
                .anyRequest().permitAll()
                .and()
                .formLogin().and()
                .logout()
                .logoutSuccessUrl("/")
                .and()
                .addFilterBefore(new JwtTokenFilter(jwtProperties.getSecretKey(), userUpdateService), UsernamePasswordAuthenticationFilter.class)
                .build();
    }

}

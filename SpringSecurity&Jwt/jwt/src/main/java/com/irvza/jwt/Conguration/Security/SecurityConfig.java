package com.irvza.jwt.Conguration.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.irvza.jwt.Conguration.Security.Filters.JwtAuthenticationFilter;
import com.irvza.jwt.Conguration.Security.Filters.JwtAutorizationFilter;
import com.irvza.jwt.Conguration.Security.Service.UserDetailServiceImpl;
import com.irvza.jwt.Conguration.Security.jwt.JwtUtils;

@Configuration
public class SecurityConfig {

    @Autowired
    UserDetailServiceImpl userDetailServiceImpl;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    JwtAutorizationFilter authAutorizationFilter;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, AuthenticationManager authenticationManager ) throws Exception{

        JwtAuthenticationFilter jwtAuthenticationFilter= new JwtAuthenticationFilter(jwtUtils, authenticationManager);
        jwtAuthenticationFilter.setAuthenticationManager(authenticationManager);
        jwtAuthenticationFilter.setFilterProcessesUrl("/api/v1/login"); //Ruta de login

        return httpSecurity
            .csrf(config->config.disable())
            .authorizeHttpRequests(auth -> {
                auth.requestMatchers("/api/v1/mensaje").permitAll();
                auth.anyRequest().authenticated();
            })
            .sessionManagement(session -> {
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
            })
            .addFilter(jwtAuthenticationFilter)
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            .build();
    }

    @Bean
    PasswordEncoder passwordEncoder(){

        return new BCryptPasswordEncoder();
    }


    @SuppressWarnings("removal")
    @Bean 
    AuthenticationManager authenticationManager(HttpSecurity httpSecurity, PasswordEncoder passwordEncoder) throws Exception{

        return httpSecurity.getSharedObject(AuthenticationManagerBuilder.class)
                    .userDetailsService(userDetailServiceImpl)
                    .passwordEncoder(passwordEncoder)
                    .and()
                    .build();

    }


}

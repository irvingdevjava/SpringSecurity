package com.irvza.security.Configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    //Configuration one
    // @Bean
    // public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception{

    //     return httpSecurity
    //         .authorizeHttpRequests()
    //             .requestMatchers("/api/v1/m1").permitAll()
    //             .anyRequest().authenticated()
    //         .and()
    //         .formLogin().permitAll()
    //         .and()
    //         .build()
    //     ;

    // }


    //Configuration two
    @Bean
    SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception{
        return httpSecurity
            .authorizeHttpRequests(auth -> {
                auth.requestMatchers("/api/v1/m1").permitAll();
                auth.anyRequest().authenticated();
            })
            .formLogin().permitAll()
                .successHandler(successHandler())
                .permitAll()
            .and()
            .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.ALWAYS) // ALWAYS , IF_REQUIRED , NEVER , STATELESS
                .invalidSessionUrl("/login")
                .maximumSessions(1)
                .expiredUrl("/login")
                .sessionRegistry(sessionRegistry())
            .and()
            .sessionFixation()
                .migrateSession()
            .and()
            .httpBasic()
            .and()
            .build();

    }

    @Bean
    public SessionRegistry sessionRegistry(){
        return new SessionRegistryImpl();
    }


    public AuthenticationSuccessHandler successHandler(){
        return ((request, response, authentication)->{
            response.sendRedirect("/api/v1/m3");
        });
    }






}

package com.irvza.jwt.Conguration.Security.Filters;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.irvza.jwt.Conguration.Security.Service.UserDetailServiceImpl;
import com.irvza.jwt.Conguration.Security.jwt.JwtUtils;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;

@Component
public class JwtAutorizationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAutorizationFilter.class);

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    private UserDetailServiceImpl userDetailServiceImpl;

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request,
                                    @NotNull HttpServletResponse response,
                                    @NotNull FilterChain filterChain) throws IOException, ServletException {

        String tokenHeader = request.getHeader("Authorization");
        logger.info("Authorization header: {}", tokenHeader);

        if (tokenHeader != null && tokenHeader.startsWith("Bearer ")) {
            String token = tokenHeader.substring(7);
            logger.info("Token extracted: {}", token);

            if (jwtUtils.isTokenValid(token)) {
                String username = jwtUtils.getUsernameFromToken(token);
                logger.info("Username from token: {}", username);

                UserDetails userDetails = userDetailServiceImpl.loadUserByUsername(username);
                if (userDetails != null) {
                    UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    logger.info("Authentication set for user: {}", username);
                } else {
                    logger.warn("UserDetails not found for username: {}", username);
                }
            } else {
                logger.warn("Invalid token: {}", token);
            }
        } else {
            logger.warn("Authorization header is either null or does not start with 'Bearer '");
        }

        filterChain.doFilter(request, response);
    }
}

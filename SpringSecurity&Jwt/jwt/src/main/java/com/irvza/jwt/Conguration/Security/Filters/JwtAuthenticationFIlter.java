package com.irvza.jwt.Conguration.Security.Filters;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.irvza.jwt.Conguration.Security.jwt.JwtUtils;
import com.irvza.jwt.Modelos.UserEntity;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtAuthenticationFIlter extends UsernamePasswordAuthenticationFilter{

    @Autowired
    JwtUtils jwtUtils;

    public JwtAuthenticationFIlter(JwtUtils jwtUtils){
        this.jwtUtils=jwtUtils;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, 
                                                HttpServletResponse response)
            throws AuthenticationException {
                UserEntity userEntity=null;
                String username="";
                String password="";

                try {
                    userEntity= new ObjectMapper().readValue(request.getInputStream(), UserEntity.class);
                    username=userEntity.getUsername();
                    password=userEntity.getPassword();
                }catch (StreamReadException e) {
                    throw new RuntimeException("Error al leer el flujo de datos", e);
                } catch (DatabindException e) {
                    throw new RuntimeException("Error de mapeo de datos", e);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException("Error de procesamiento de JSON", e);
                } catch (IOException e) {
                    throw new RuntimeException("Error de entrada/salida", e);
                } catch (Exception e) {
                    throw new RuntimeException("Error general", e);
                }

                UsernamePasswordAuthenticationToken authenticationToken=new UsernamePasswordAuthenticationToken(username, password);
                
        return getAuthenticationManager().authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, 
                                            HttpServletResponse response, FilterChain chain,
            Authentication authResult) throws IOException, ServletException {
        User user=(User) authResult.getPrincipal();
        String token=jwtUtils.GenerateAccessToken(user.getUsername());

        response.addHeader("Authorization", token);

        Map<String, Object> httpResponse=new HashMap();
        httpResponse.put("token", token);
        httpResponse.put("Message", "Authenticacion correcta");
        httpResponse.put("Username", user.getUsername());

        response.getWriter().write(new ObjectMapper().writeValueAsString(httpResponse));
        response.setStatus(HttpStatus.OK.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().flush();

        super.successfulAuthentication(request, response, chain, authResult);
    }


}

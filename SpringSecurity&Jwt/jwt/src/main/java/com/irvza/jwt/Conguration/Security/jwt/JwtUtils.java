package com.irvza.jwt.Conguration.Security.jwt;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JwtUtils {

    @Value("${jwt.secret.key}")
    private String privateKey;
    @Value("${jwt.time.expiration}")
    private String timeExpiration;  

    //Crear un token
    public String GenerateAccessToken(String username){
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + Long.parseLong(timeExpiration)))
                .signWith(getSigntureKey(),  SignatureAlgorithm.HS256)
                .compact();
    }
    
    //Validar token de acceso
    public boolean isTokenValid(String token){
        try {
            Jwts.parser()
                .setSigningKey(getSigntureKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
            return true;
        } catch (Exception e) {
            log.error("Token invalido, error".concat(e.getMessage()));
            return false;
        }
    }

    //Obtener username del token
    public String getUsernameFromToken(String token){
        return getClaim(token, Claims::getSubject);
    }

    //Obtener un solo clamin
    public <T> T getClaim(String token, Function<Claims, T> claimsFunction ){
        Claims claims=extractAllClaims(token);
        return claimsFunction.apply(claims);

    }


    //Obtencion de claims del token
    public Claims extractAllClaims(String token){
        return Jwts.parser()
        .setSigningKey(getSigntureKey())
        .build()
        .parseClaimsJws(token)
        .getBody();
    }


    //Obtener firma del token
    public Key getSigntureKey(){
        byte[] keyBytes= Decoders.BASE64.decode(privateKey);
        return Keys.hmacShaKeyFor(keyBytes);

    }

}

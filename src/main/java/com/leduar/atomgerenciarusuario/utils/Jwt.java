package com.leduar.atomgerenciarusuario.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;

import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

public class Jwt {

    static String secret = "asdfSFS34wfsdfsdfSDSD32dfsddDDerQSNCK34SOWEK5354fdgdf4";
    static Key hmacKey = new SecretKeySpec(Base64.getDecoder().decode(secret),
            SignatureAlgorithm.HS256.getJcaName());


    public static String gerarToken(String firstName, Long id) {
        return Jwts.builder()
                .claim("firstName", firstName)
                .claim("id", id)
                .setSubject(firstName)
                .setId(UUID.randomUUID().toString())
                .setIssuedAt(Date.from(new Date().toInstant()))
                .setExpiration(Date.from(new Date().toInstant().plus(5l, ChronoUnit.MINUTES)))
                .signWith(hmacKey)
                .compact();
    }

    public static Jws<Claims> validateToken(String tokenJwt) {
        return Jwts.parserBuilder()
                .setSigningKey(hmacKey)
                .build()
                .parseClaimsJws(tokenJwt);
    }
}

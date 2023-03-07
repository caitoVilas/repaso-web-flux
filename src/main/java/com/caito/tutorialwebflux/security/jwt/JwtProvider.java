package com.caito.tutorialwebflux.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
@Slf4j
public class JwtProvider {
    @Value("${jwt.secret")
    private String secret;
    @Value("${jwt.expiration")
    private Integer expiration;

    public Key getKey(String secret){
        byte[] secretByte = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(secretByte);
    }

    public String generateToken(UserDetails userDetails){
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .claim("roles", userDetails.getAuthorities())
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + expiration *1000))
                .signWith(getKey(secret))
                .compact();
    }

    public Claims getClaims(String token){
        return Jwts.parserBuilder().setSigningKey(getKey(secret)).build().parseClaimsJws(token).getBody();
    }

    public String getSubject(String token){
        return Jwts.parserBuilder().setSigningKey(getKey(secret)).build().parseClaimsJws(token).getBody()
                .getSubject();
    }

    public boolean validateToken(String token){
        try {
            Jwts.parserBuilder().setSigningKey(getKey(secret)).build().parseClaimsJws(token).getBody();
            return true;
        }catch (ExpiredJwtException e){
            log.error("token Expirado");
        }catch (UnsupportedJwtException e){
            log.error("token no soportado");
        }catch (SignatureException e){
            log.error("error en la firma");
        }catch (IllegalArgumentException e){
            log.error("argumentos no validos");
        }
        return false;
    }
}

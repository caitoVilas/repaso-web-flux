package com.caito.tutorialwebflux.security.jwt;

import com.caito.tutorialwebflux.exception.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@Slf4j
public class JwtAuthenticationManager implements ReactiveAuthenticationManager {
    @Autowired
    private JwtProvider jwtProvider;

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        return Mono.just(authentication)
                .map(auth -> jwtProvider.getClaims(auth.getCredentials().toString()))
                .log()
                .onErrorResume(e -> Mono.error(new CustomException(HttpStatus.UNAUTHORIZED, "no autorizado")))
                .map(claims -> new UsernamePasswordAuthenticationToken(claims.getSubject(),null,
                        Stream.of(claims.get("roles")).map(role -> (List<Map<String, String>>) role)
                                .flatMap(role -> role.stream()
                                        .map(r -> r.get("authoritiy"))
                                        .map(SimpleGrantedAuthority::new))
                                .collect(Collectors.toList())));
    }
}

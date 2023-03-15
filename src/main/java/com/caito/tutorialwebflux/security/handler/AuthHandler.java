package com.caito.tutorialwebflux.security.handler;

import com.caito.tutorialwebflux.security.dto.LoginDTO;
import com.caito.tutorialwebflux.security.dto.TokenDTO;
import com.caito.tutorialwebflux.security.dto.UserNewDTO;
import com.caito.tutorialwebflux.security.entity.User;
import com.caito.tutorialwebflux.security.service.contract.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

/**
 * @author caito Vilas
 */

@Component
@Slf4j
public class AuthHandler {
    @Autowired
    private UserService userService;

    public Mono<ServerResponse> login(ServerRequest request){
        Mono<LoginDTO> dtoMono = request.bodyToMono(LoginDTO.class);
        return dtoMono.flatMap(dto -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(
                userService.login(dto), TokenDTO.class));
    }

    public Mono<ServerResponse> create(ServerRequest request){
        Mono<UserNewDTO> dtoMono = request.bodyToMono(UserNewDTO.class);
        return dtoMono.flatMap(dto -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(
                userService.createUser(dto), User.class));
    }
}

package com.caito.tutorialwebflux.security.service.contract;

import com.caito.tutorialwebflux.security.dto.LoginDTO;
import com.caito.tutorialwebflux.security.dto.TokenDTO;
import com.caito.tutorialwebflux.security.dto.UserNewDTO;
import com.caito.tutorialwebflux.security.entity.User;
import reactor.core.publisher.Mono;

public interface UserService {
    Mono<TokenDTO> login(LoginDTO dto);
    Mono<User> createUser(UserNewDTO dto);
}

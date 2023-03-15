package com.caito.tutorialwebflux.security.service.impl;

import com.caito.tutorialwebflux.exception.CustomException;
import com.caito.tutorialwebflux.security.dto.LoginDTO;
import com.caito.tutorialwebflux.security.dto.TokenDTO;
import com.caito.tutorialwebflux.security.dto.UserNewDTO;
import com.caito.tutorialwebflux.security.entity.User;
import com.caito.tutorialwebflux.security.enums.Role;
import com.caito.tutorialwebflux.security.jwt.JwtProvider;
import com.caito.tutorialwebflux.security.repository.UserRepository;
import com.caito.tutorialwebflux.security.service.contract.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtProvider jwtProvider;
    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    public Mono<TokenDTO> login(LoginDTO dto) {
        log.info("incio servicio login");
        return userRepository.findByUsernameOrEmail(dto.getUsername(), dto.getUsername())
                .filter(user -> passwordEncoder.matches(dto.getPassword(), user.getPassword()))
                .map(user -> new TokenDTO(jwtProvider.generateToken(user)))
                .switchIfEmpty(Mono.error(new CustomException(HttpStatus.BAD_REQUEST,
                        "credenciales invalidas")));
    }

    @Override
    public Mono<User> createUser(UserNewDTO dto) {
        log.info("inicio sercio alta usuario");
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRoles(Role.ROLE_ADMIN.name());
        Mono<Boolean> userExist = userRepository.findByUsernameOrEmail(user.getUsername(),
                user.getEmail()).hasElement();
        return userExist
                .flatMap(exists -> exists ?
                        Mono.error(new CustomException(HttpStatus.BAD_REQUEST, "ya existe el usuario"))
                        : userRepository.save(user));
    }
}

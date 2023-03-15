package com.caito.tutorialwebflux.security.router;

import com.caito.tutorialwebflux.security.handler.AuthHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
@Slf4j
 class AuthRouter {
    private static final String PATH = "auth/**";

    @Bean
    RouterFunction<ServerResponse> routerAuth(AuthHandler handler){
        return RouterFunctions.route()
                .POST(PATH + "login", handler::login)
                .POST(PATH + "create", handler::create)
                .build();
    }
}

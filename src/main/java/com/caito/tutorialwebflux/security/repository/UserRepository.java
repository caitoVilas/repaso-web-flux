package com.caito.tutorialwebflux.security.repository;

import com.caito.tutorialwebflux.security.entity.User;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends ReactiveCrudRepository<User, Long> {
    Mono<User> findByUsernameOrEmail(String username, String email);
}

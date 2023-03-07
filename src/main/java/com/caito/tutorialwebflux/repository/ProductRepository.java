package com.caito.tutorialwebflux.repository;

import com.caito.tutorialwebflux.entity.Product;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface ProductRepository extends ReactiveCrudRepository<Product, Integer> {
    Mono<Product> findByName(String name);
    @Query("SELECT * FROM products WHERE id <> :id AND name = :name")
    Mono<Product> repeatName(Integer id, String name);
}

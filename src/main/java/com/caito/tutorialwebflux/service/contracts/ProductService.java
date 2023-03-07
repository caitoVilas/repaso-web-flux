package com.caito.tutorialwebflux.service.contracts;

import com.caito.tutorialwebflux.dto.ProductoNuevoDTO;
import com.caito.tutorialwebflux.entity.Product;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductService {
    Flux<Product> getAll();
    Mono<Product> getById(Integer id);
    Mono<Product> save(ProductoNuevoDTO product);
    Mono<Product> update(Integer id, ProductoNuevoDTO product);
    Mono<Void> delete(Integer id);
}

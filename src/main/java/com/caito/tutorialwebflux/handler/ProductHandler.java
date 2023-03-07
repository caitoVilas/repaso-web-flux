package com.caito.tutorialwebflux.handler;

import com.caito.tutorialwebflux.dto.ProductoNuevoDTO;
import com.caito.tutorialwebflux.entity.Product;
import com.caito.tutorialwebflux.service.contracts.ProductService;
import com.caito.tutorialwebflux.validation.ObjectValidation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class ProductHandler {
    @Autowired
    private ProductService productService;
    @Autowired
    private ObjectValidation objectValidation;

    public Mono<ServerResponse> getAll(ServerRequest request) {
        Flux<Product> products = productService.getAll();
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(products, Product.class);
    }

    public Mono<ServerResponse> getById(ServerRequest request) {
        Integer id = Integer.valueOf(request.pathVariable("id"));
        Mono<Product> product = productService.getById(id);
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(product, Product.class);
    }

    public Mono<ServerResponse> save(ServerRequest request) {
        Mono<ProductoNuevoDTO> product = request.bodyToMono(ProductoNuevoDTO.class)
                .doOnNext(objectValidation::validate);
        return product.flatMap(p -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                .body(productService.save(p), ProductoNuevoDTO.class));
    }

    public Mono<ServerResponse> update(ServerRequest request) {
        Mono<ProductoNuevoDTO> product = request.bodyToMono(ProductoNuevoDTO.class)
                .doOnNext(objectValidation::validate);
        Integer id = Integer.valueOf(request.pathVariable("id"));
        return product.flatMap(p -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                .body(productService.update(id, p), ProductoNuevoDTO.class));
    }

    public Mono<ServerResponse> delete(ServerRequest request) {
        Integer id = Integer.valueOf(request.pathVariable("id"));
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                .body(productService.delete(id), Product.class);
    }
}
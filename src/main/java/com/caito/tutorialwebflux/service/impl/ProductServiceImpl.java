package com.caito.tutorialwebflux.service.impl;

import com.caito.tutorialwebflux.dto.ProductoNuevoDTO;
import com.caito.tutorialwebflux.entity.Product;
import com.caito.tutorialwebflux.exception.CustomException;
import com.caito.tutorialwebflux.repository.ProductRepository;
import com.caito.tutorialwebflux.service.contracts.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;


    @Override
    public Flux<Product> getAll() {
        return productRepository.findAll();
    }

    @Override
    public Mono<Product> getById(Integer id) {
        return productRepository.findById(id).switchIfEmpty(Mono.error(
                new CustomException(HttpStatus.NOT_FOUND, "producto no encontrado")));
    }

    @Override
    public Mono<Product> save(ProductoNuevoDTO product) {
        Mono<Boolean> existsByName = productRepository.findByName(product.getName()).hasElement();
        return existsByName.flatMap(exists -> exists ? Mono.error(new
                CustomException(HttpStatus.BAD_REQUEST, "el nombre esta en uso")):
                productRepository.save(new Product(null,product.getName(), product.getPrice())));
    }

    @Override
    public Mono<Product> update(Integer id, ProductoNuevoDTO product) {
        Mono<Boolean> productId = productRepository.findById(id).hasElement();
        Mono<Boolean> productRepeatName = productRepository.repeatName(id, product.getName()).hasElement();
        return productId.flatMap(existsId -> existsId ? productRepeatName.flatMap(existsName -> existsName ?
                Mono.error(new CustomException(HttpStatus.BAD_REQUEST, "el nombre de producto esta en uso")):
                productRepository.save(new Product(id, product.getName(), product.getPrice()))):
                Mono.error(new CustomException(HttpStatus.NOT_FOUND, "el producto no existe")));
    }

    @Override
    public Mono<Void> delete(Integer id) {
        Mono<Boolean> exixts = productRepository.findById(id).hasElement();
        return exixts.flatMap(e -> e ? productRepository.deleteById(id) :
                Mono.error(new CustomException(HttpStatus.NOT_FOUND, "el producto no existe")));
    }
}

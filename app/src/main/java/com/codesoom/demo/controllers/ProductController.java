package com.codesoom.demo.controllers;

// REST API
// TODO: GET /products
// TODO: GET /products/{id}
// TODO: POST /products
// TODO: PATCH /products/{id}
// TODO: DELETE /products/{id}

import com.codesoom.demo.domain.Product;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {
    @GetMapping
    public List<Product> list() {
        Product product = new Product(1L, "쥐돌이", "냥이월드", 3000);
        return List.of(product);
    }
}

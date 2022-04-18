package com.codesoom.demo.controllers;

// REST API
// TODO: GET /products
// TODO: GET /products/{id}
// TODO: POST /products
// TODO: PATCH /products/{id}
// TODO: DELETE /products/{id}

import com.codesoom.demo.application.ProductService;
import com.codesoom.demo.domain.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public List<Product> list() {
        return productService.getProducts();
    }

    @GetMapping("{id}")
    public Product detail(@PathVariable Long id) {
        return productService.getProduct(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Product create(@RequestBody Product product) {
        return productService.createProduct(product);
    }

    @PatchMapping("{id}")
    public Product update(@PathVariable Long id, @RequestBody Product source) {
        return productService.updateProduct(id, source);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        productService.deleteProduct(id);
    }
}

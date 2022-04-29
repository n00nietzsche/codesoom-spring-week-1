package com.codesoom.demo.controllers;

// REST API
// TODO: GET /products
// TODO: GET /products/{id}
// TODO: POST /products
// TODO: PATCH /products/{id}
// TODO: DELETE /products/{id}

// /products -> Create, Read
// /products/{id} -> Read, Update, Delete

import com.codesoom.demo.application.AuthenticationService;
import com.codesoom.demo.application.ProductService;
import com.codesoom.demo.domain.Product;
import com.codesoom.demo.dto.ProductDto;
import com.codesoom.demo.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    private final AuthenticationService authenticationService;

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
    public Product create(@RequestBody @Valid ProductDto productDto,
                          @RequestHeader("Authorization") String authorization) {
        String token = authorization.substring("Bearer ".length());
        Long userId = authenticationService.parseToken(token);
        System.out.println("userId = " + userId);
        return productService.createProduct(productDto);
    }

    @PatchMapping("{id}")
    public Product update(@PathVariable Long id, @RequestBody @Valid ProductDto productDto) {
        return productService.updateProduct(id, productDto);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        productService.deleteProduct(id);
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public void handleMissingRequestHeaderException() {
        // Authorization 헤더가 필요한데 들어오지 않은 상황임.
    }

    @ExceptionHandler(JwtException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public void handleJwtException() {
        // 어떤 방식으로든 JWT 를 해석하다가 예외가 터진 경우
    }
}

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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

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
    // `@PreAuthorize` 애노테이션이 붙어있으면
    // 요소로 받은 메서드의 결과에 따라 이 메서드를 실행할 수 있고 없고가 결정된다.
    // hasAuthority() 로 권한을 설정할 수도 있다.
    @PreAuthorize("isAuthenticated() and hasAuthority('USER')")
    // 이건 로그인이 필요해! -> Authorization
    // 누가 이걸 하는 거야? -> Authentication
    public Product create(@RequestBody @Valid ProductDto productDto) {
        return productService.createProduct(productDto);
    }

    @PatchMapping("{id}")
    @PreAuthorize("isAuthenticated()")
    // 이건 로그인이 필요해! -> Authorization
    // 누가 이걸 하는 거야? -> Authentication
    public Product update(@PathVariable Long id, @RequestBody @Valid ProductDto productDto) {
        return productService.updateProduct(id, productDto);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("isAuthenticated()")
    // 이건 로그인이 필요해! -> Authorization
    // 누가 이걸 하는 거야? -> Authentication
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

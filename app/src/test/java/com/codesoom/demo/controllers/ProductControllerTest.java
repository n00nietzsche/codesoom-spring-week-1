package com.codesoom.demo.controllers;

import com.codesoom.demo.Utf8WebMvcTest;
import com.codesoom.demo.application.ProductService;
import com.codesoom.demo.domain.Product;
import com.codesoom.demo.dto.ProductDto;
import com.codesoom.demo.exceptions.ProductNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// REST API
// TODO: GET /products
// TODO: GET /products/{id}
// TODO: POST /products
// TODO: PATCH /products/{id}
// TODO: DELETE /products/{id}

@Utf8WebMvcTest(ProductController.class)
class ProductControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @BeforeEach
    void setUp() {
        Product product = Product.builder()
                .name("쥐돌이")
                .maker("냥이월드")
                .price(5000)
                .build();

        given(productService.getProducts()).willReturn(List.of(product));
        given(productService.getProduct(1L)).willReturn(product);
        given(productService.getProduct(1000L)).willThrow(new ProductNotFoundException(1000L));
        given(productService.createProduct(any(ProductDto.class))).willReturn(product);
        given(productService.updateProduct(eq(1L), any(ProductDto.class))).will(invocation -> {
            Long id = invocation.getArgument(0);
            ProductDto productDto = invocation.getArgument(1);

            return Product.builder()
                    .id(id)
                    .name("쥐순이")
                    .maker(productDto.getMaker())
                    .price(productDto.getPrice())
                    .build();
        });
        given(productService.updateProduct(eq(1000L), any(ProductDto.class))).willThrow(new ProductNotFoundException(1000L));
        given(productService.deleteProduct(1000L)).willThrow(new ProductNotFoundException(1000L));
    }

    @Test
    void list() throws Exception {
        // 목록을 이미 갖추고 있음
        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("쥐돌이")));
    }

    @Test
    void detail() throws Exception {
        // 목록을 이미 갖추고 있음
        mockMvc.perform(get("/products/1"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("쥐돌이")));
    }

    @Test
    void detail_notFound() throws Exception {
        // 목록을 이미 갖추고 있음
        mockMvc.perform(get("/products/1000"))
                .andExpect(status().isNotFound());
    }

    @Test
    void create() throws Exception {
        // 목록을 이미 갖추고 있음
        mockMvc.perform(post("/products")
                        .content("{\"name\": \"쥐돌이\", \"maker\": \"냥이월드\", \"price\": 5000}")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andExpect(content().string(containsString("쥐돌이")));

        verify(productService).createProduct(any(ProductDto.class));
    }

    @Test
    void update() throws Exception {
        // 목록을 이미 갖추고 있음
        mockMvc.perform(patch("/products/1")
                        .content("{\"name\": \"쥐순이\", \"maker\": \"냥이월드\", \"price\": 5000}")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("쥐순이")));

        verify(productService).updateProduct(eq(1L), any(ProductDto.class));
    }

    @Test
    void updateNotFound() throws Exception {
        // 목록을 이미 갖추고 있음
        mockMvc.perform(patch("/products/1000")
                        .content("{\"name\": \"쥐순이\", \"maker\": \"냥이월드\", \"price\": 5000}")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound());
    }

    @Test
    void update_InvalidAttributes() throws Exception {
        // 목록을 이미 갖추고 있음
        mockMvc.perform(patch("/products/1")
                        .content("{\"name\": \"\", \"maker\": \"\", \"price\": -100}")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    void remove() throws Exception {
        // 목록을 이미 갖추고 있음
        mockMvc.perform(delete("/products/1"))
                .andExpect(status().isNoContent());

        verify(productService).deleteProduct(1L);
    }

    @Test
    void removeNotFound() throws Exception {
        // 목록을 이미 갖추고 있음
        mockMvc.perform(delete("/products/1000"))
                .andExpect(status().isNotFound());
    }

    @Test
    void create_InvalidAttributes() throws Exception {
        // 목록을 이미 갖추고 있음
        mockMvc.perform(post("/products")
                        .content("{\"name\": \"\", \"maker\": \"\", \"price\": -100}")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest());
    }
}
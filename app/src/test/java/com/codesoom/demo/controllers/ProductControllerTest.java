package com.codesoom.demo.controllers;

import com.codesoom.demo.Utf8WebMvcTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
    @Autowired
    private ProductController productController;

    @Test
    void list() throws Exception {
        // 목록을 이미 갖추고 있음
        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("쥐돌이")));
    }
}
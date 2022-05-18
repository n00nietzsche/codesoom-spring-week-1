package com.codesoom.demo.controllers;

import com.codesoom.demo.annotations.Utf8MockMvc;
import com.codesoom.demo.application.AuthenticationService;
import com.codesoom.demo.application.ProductService;
import com.codesoom.demo.application.UserService;
import com.codesoom.demo.domain.Product;
import com.codesoom.demo.domain.UserRepository;
import com.codesoom.demo.dto.ProductDto;
import com.codesoom.demo.dto.UserCreationDto;
import com.codesoom.demo.exceptions.ProductNotFoundException;
import com.codesoom.demo.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// REST API
// TODO: GET /products
// TODO: GET /products/{id}
// TODO: POST /products
// TODO: PATCH /products/{id}
// TODO: DELETE /products/{id}

@SpringBootTest
@Utf8MockMvc
@AutoConfigureRestDocs
class ProductControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @MockBean
    private ProductService productService;

    private static final String VALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9.ZZ3CUl0jxeLGvQ1Js5nG2Ty5qGTlqai5ubDMXZOdaDk";
    private static final String INVALID_TOKEN = VALID_TOKEN + "A";

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        userService.createUser(UserCreationDto
                .builder()
                        .name("NAME")
                        .email("EMAIL@naver.com")
                        .password("123123123")
                .build());

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
    void jwtUtil() {
        Claims claims = jwtUtil.decode(VALID_TOKEN);
        Long userId = claims.get("userId", Long.class);
        System.out.println("userId = " + userId);
    }

    @Test
    void list() throws Exception {
        // 목록을 이미 갖추고 있음
        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("쥐돌이")))
                .andDo(document("get-products"));
    }

    @Test
    void detail() throws Exception {
        // 목록을 이미 갖추고 있음
        mockMvc.perform(get("/products/1"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("쥐돌이")))
                .andDo(document("get-product"));
    }

    @Test
    void detail_notFound() throws Exception {
        // 목록을 이미 갖추고 있음
        mockMvc.perform(get("/products/1000"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createWithAccessToken() throws Exception {
        // 목록을 이미 갖추고 있음
        mockMvc.perform(post("/products")
                        .content("{\"name\": \"쥐돌이\", \"maker\": \"냥이월드\", \"price\": 5000}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + VALID_TOKEN)
                )
                .andExpect(status().isCreated())
                .andExpect(content().string(containsString("쥐돌이")));

        verify(productService).createProduct(any(ProductDto.class));
    }

    @Test
    void createWithoutAccessToken() throws Exception {
        // 목록을 이미 갖추고 있음
        mockMvc.perform(post("/products")
                        .content("{\"name\": \"쥐돌이\", \"maker\": \"냥이월드\", \"price\": 5000}")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isUnauthorized());
    }

    @Test
    void createWithInvalidAccessToken() throws Exception {
        // 목록을 이미 갖추고 있음
        mockMvc.perform(post("/products")
                        .content("{\"name\": \"쥐돌이\", \"maker\": \"냥이월드\", \"price\": 5000}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + INVALID_TOKEN)
                )

                .andExpect(status().isUnauthorized());
    }

    @Test
    void updateWithAccessToken() throws Exception {
        // 목록을 이미 갖추고 있음
        mockMvc.perform(patch("/products/1")
                        .content("{\"name\": \"쥐순이\", \"maker\": \"냥이월드\", \"price\": 5000}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + VALID_TOKEN)
                )
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("쥐순이")));

        verify(productService).updateProduct(eq(1L), any(ProductDto.class));
    }

    @Test
    void updateWithoutAccessToken() throws Exception {
        // 목록을 이미 갖추고 있음
        mockMvc.perform(patch("/products/1")
                        .content("{\"name\": \"쥐순이\", \"maker\": \"냥이월드\", \"price\": 5000}")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isUnauthorized());
    }

    @Test
    void updateWithInvalidAccessToken() throws Exception {
        // 목록을 이미 갖추고 있음
        mockMvc.perform(patch("/products/1")
                        .content("{\"name\": \"쥐순이\", \"maker\": \"냥이월드\", \"price\": 5000}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + INVALID_TOKEN)
                )
                .andExpect(status().isUnauthorized());
    }

    @Test
    void updateNotFound() throws Exception {
        // 목록을 이미 갖추고 있음
        mockMvc.perform(patch("/products/1000")
                        .content("{\"name\": \"쥐순이\", \"maker\": \"냥이월드\", \"price\": 5000}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + VALID_TOKEN)
                )
                .andExpect(status().isNotFound());
    }

    @Test
    void update_InvalidAttributes() throws Exception {
        // 목록을 이미 갖추고 있음
        mockMvc.perform(patch("/products/1")
                        .content("{\"name\": \"\", \"maker\": \"\", \"price\": -100}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + VALID_TOKEN)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    void removeWithAccessToken() throws Exception {
        // 목록을 이미 갖추고 있음
        mockMvc.perform(
                delete("/products/1")
                        .header("Authorization", "Bearer " + VALID_TOKEN)
                )
                .andExpect(status().isNoContent());

        verify(productService).deleteProduct(1L);
    }

    @Test
    void removeWithInvalidAccessToken() throws Exception {
        // 목록을 이미 갖추고 있음
        mockMvc.perform(
                        delete("/products/1")
                                .header("Authorization", "Bearer " + INVALID_TOKEN)
                )
                .andExpect(status().isUnauthorized());
    }

    @Test
    void removeWithoutAccessToken() throws Exception {
        // 목록을 이미 갖추고 있음
        mockMvc.perform(
                        delete("/products/1")
                )
                .andExpect(status().isUnauthorized());
    }

    @Test
    void removeNotFound() throws Exception {
        // 목록을 이미 갖추고 있음
        mockMvc.perform(delete("/products/1000")
                        .header("Authorization", "Bearer " + VALID_TOKEN))
                .andExpect(status().isNotFound());
    }

    @Test
    void create_InvalidAttributes() throws Exception {
        // 목록을 이미 갖추고 있음
        mockMvc.perform(post("/products")
                        .content("{\"name\": \"\", \"maker\": \"\", \"price\": -100}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + VALID_TOKEN)
                )
                .andExpect(status().isBadRequest());
    }
}
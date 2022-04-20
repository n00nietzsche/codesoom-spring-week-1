package com.codesoom.demo.domain;

import com.codesoom.demo.dto.ProductDto;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ProductTest {
    @Test
    void creationWithId() {
        String name = "쥐돌이";
        long id = 1L;

        Product product = Product.builder()
                .id(id)
                .name("쥐돌이")
                .maker("냥이월드")
                .price(5000)
                .build();

        assertThat(product.getId()).isEqualTo(id);
        assertThat(product.getName()).isEqualTo(name);
    }

    @Test
    void creationWithout() {
        String name = "쥐돌이";

        Product product = Product.builder()
                .name("쥐돌이")
                .maker("냥이월드")
                .price(5000)
                .build();

        assertThat(product.getName()).isEqualTo(name);
    }

    @Test
    void change() {
        Product product = Product.builder()
                .id(1L)
                .name("쥐돌이")
                .maker("냥이월드")
                .price(3000)
                .build();

        ProductDto productDto = ProductDto.builder()
                .name("쥐순이")
                .maker("냥이월드 2탄")
                .price(5000)
                .build();

        product.change(productDto);

        assertThat(product.getName()).isEqualTo("쥐순이");
        assertThat(product.getMaker()).isEqualTo("냥이월드 2탄");
        assertThat(product.getPrice()).isEqualTo(5000);
    }
}
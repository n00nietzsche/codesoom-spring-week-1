package com.codesoom.demo.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ProductTest {
    @Test
    void creationWithId() {
        String name = "쥐돌이";
        long id = 1L;

        Product product = new Product(id,"쥐돌이", "냥이월드", 5000);
        assertThat(product.getId()).isEqualTo(id);
        assertThat(product.getName()).isEqualTo(name);
    }

    @Test
    void creationWithout() {
        String name = "쥐돌이";

        Product product = new Product("쥐돌이", "냥이월드", 5000);
        assertThat(product.getName()).isEqualTo(name);
    }

    @Test
    void change() {
        Product product = new Product(1L, "쥐돌이", "냥이월드", 3000);
        Product source = new Product("쥐순이", "냥이월드 2탄", 5000);

        product.change(source);

        assertThat(product.getName()).isEqualTo("쥐순이");
        assertThat(product.getMaker()).isEqualTo("냥이월드 2탄");
        assertThat(product.getPrice()).isEqualTo(5000);
    }
}
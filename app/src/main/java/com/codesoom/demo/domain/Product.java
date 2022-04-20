package com.codesoom.demo.domain;

// 고양이 장난감 쇼핑몰
// Product 모델
// User 모델
// Order 모델
// ... 모델
// Application (UseCase)
// Product -> 관리자 등록/수정/삭제 -> list/detail
// 주문 -> 확인 -> 배송 등의 처리

// Product
// 0. 식별자 - identifier (ID)
// 1. 이름 - 쥐돌이
// 2. 제조사 - 냥이월드
// 3. 가격 - 5,000원 (판매가)
// 4. 이미지 - static, CDN => image URL

import com.codesoom.demo.dto.ProductDto;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String maker;
    private Integer price;
    private String imageUrl;

    public void change(ProductDto productDto) {
        this.setName(productDto.getName());
        this.setMaker(productDto.getMaker());
        this.setPrice(productDto.getPrice());
        this.setImageUrl(productDto.getImageUrl());
    }
}

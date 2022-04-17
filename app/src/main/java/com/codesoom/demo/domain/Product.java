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

public class Product {
    private Long id;
    private String name;
    private String maker;
    private Integer price;
    private String imageUrl;

    public Product() {
    }

    public Product(Long id, String name, String maker, Integer price) {
        this.id = id;
        this.name = name;
        this.maker = maker;
        this.price = price;
    }

    public Product(String name, String maker, Integer price) {
        this.name = name;
        this.maker = maker;
        this.price = price;
    }

    public Product(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Long getId() {
        return id;
    }

    public String getMaker() {
        return maker;
    }

    public Integer getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}

package com.codesoom.demo.exceptions;

public class ProductNotFoundException extends RuntimeException{
    public ProductNotFoundException(Long id) {
        super("Product id: " + id + " is not found");
    }
}

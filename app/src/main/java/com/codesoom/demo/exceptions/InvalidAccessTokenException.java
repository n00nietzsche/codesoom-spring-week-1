package com.codesoom.demo.exceptions;

public class InvalidAccessTokenException extends RuntimeException{
    public InvalidAccessTokenException(String accessToken) {
        super(String.format("Invalid access token: %s", accessToken));
    }
}

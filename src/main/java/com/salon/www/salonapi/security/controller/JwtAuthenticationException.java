package com.salon.www.salonapi.security.controller;

class JwtAuthenticationException extends RuntimeException {
    JwtAuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
}

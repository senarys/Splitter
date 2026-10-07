package com.example.demo.dto;

public record UserResponse(Long id, String name, String email) {
    // password bilerek yok — client'a asla dönmemeli
}
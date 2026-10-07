package com.example.demo.dto;

public record RoomResponse(Long id, String roomName) {
    // roomPassword bilerek yok — client'a şifre dönmeyelim
}
package com.example.demo.dto;

public record ExpenseRequest(
        String productName,
        String description,
        Double amount,
        Long userId,
        Long roomId
){}

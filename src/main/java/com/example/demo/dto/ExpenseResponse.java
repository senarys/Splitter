package com.example.demo.dto;

import java.time.LocalDateTime;

public record ExpenseResponse(
        Long id,
        String productName,
        String description,
        Double amount,
        LocalDateTime date,
        String payerName
) {}

package com.example.demo.controller;

import com.example.demo.dto.ExpenseRequest;
import com.example.demo.dto.ExpenseResponse;
import com.example.demo.service.ExpenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/expenses")
@RequiredArgsConstructor
public class ExpenseController {

    private final ExpenseService expenseService;

    @PostMapping
    public ResponseEntity<ExpenseResponse> addExpense(@RequestBody ExpenseRequest request) {
        return ResponseEntity.ok(expenseService.addExpense(request));
    }

    @GetMapping("/room/{roomId}")
    public ResponseEntity<List<ExpenseResponse>> getExpensesByRoom(@PathVariable Long roomId) {
        return ResponseEntity.ok(expenseService.getExpensesByRoomId(roomId));
    }

    @GetMapping("/room/{roomId}/debts")
    public ResponseEntity<Map<Long, Double>> getDebts(@PathVariable Long roomId) {
        return ResponseEntity.ok(expenseService.calculateDebtsForRoom(roomId));
    }

    @GetMapping("/room/{roomId}/transfers")
    public ResponseEntity<List<String>> getTransfers(@PathVariable Long roomId) {
        return ResponseEntity.ok(expenseService.calculateTransfers(roomId));
    }
}
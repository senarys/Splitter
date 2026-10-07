package com.example.demo.service;

import com.example.demo.dto.ExpenseRequest;
import com.example.demo.dto.ExpenseResponse;
import com.example.demo.model.Expense;
import com.example.demo.model.Room;
import com.example.demo.model.User;
import com.example.demo.repository.ExpenseRepository;
import com.example.demo.repository.RoomRepository;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.example.demo.exception.RoomNotFoundException;
import com.example.demo.exception.UserNotFoundException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;

    public ExpenseResponse addExpense(ExpenseRequest request) {
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new UserNotFoundException("Kullanıcı bulunamadı!"));
        Room room = roomRepository.findById(request.roomId())
                .orElseThrow(() -> new RoomNotFoundException("Oda bulunamadı!"));

        Expense expense = new Expense();
        expense.setProductName(request.productName());
        expense.setDescription(request.description());
        expense.setAmount(request.amount());
        expense.setUser(user);
        expense.setRoom(room);
        expense.setDate(LocalDateTime.now());

        Expense saved = expenseRepository.save(expense);
        return toResponse(saved);
    }

    public List<ExpenseResponse> getExpensesByRoomId(Long roomId) {
        return expenseRepository.findByRoomId(roomId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public Map<Long, Double> calculateDebtsForRoom(Long roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RoomNotFoundException("Oda bulunamadı!"));
        List<Expense> expenses = expenseRepository.findByRoomId(roomId);

        double totalExpense = 0;
        for (Expense exp : expenses) {
            totalExpense += exp.getAmount();
        }

        int userCount = room.getUsers().size();
        if (userCount == 0) return new HashMap<>();

        double perPersonTarget = totalExpense / userCount;

        Map<Long, Double> userPayments = new HashMap<>();
        for (User user : room.getUsers()) {
            userPayments.put(user.getId(), 0.0);
        }

        for (Expense exp : expenses) {
            Long payerId = exp.getUser().getId();
            double currentPaid = userPayments.getOrDefault(payerId, 0.0);
            userPayments.put(payerId, currentPaid + exp.getAmount());
        }

        Map<Long, Double> finalBalances = new HashMap<>();
        for (Map.Entry<Long, Double> entry : userPayments.entrySet()) {
            double balance = entry.getValue() - perPersonTarget;
            finalBalances.put(entry.getKey(), balance);
        }
        return finalBalances;
    }

    public List<String> calculateTransfers(Long roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RoomNotFoundException("Oda bulunamadı!"));
        Map<Long, Double> finalBalances = calculateDebtsForRoom(roomId);//yerel değişken olduğu için yeniden finalBalances atarken bi üstteki metodu aldı

        List<String> transferInstructions = new ArrayList<>();

        List<Long> debtors = new ArrayList<>();
        List<Long> creditors = new ArrayList<>();

        for (Map.Entry<Long, Double> entry : finalBalances.entrySet()) {
            if (entry.getValue() < 0) {
                debtors.add(entry.getKey());
            } else if (entry.getValue() > 0) {
                creditors.add(entry.getKey());
            }
        }

        int d = 0;
        int c = 0;

        while (d < debtors.size() && c < creditors.size()) {
            Long debtorId = debtors.get(d);
            Long creditorId = creditors.get(c);

            double debt = Math.abs(finalBalances.get(debtorId));//borcu pozitif yaptı
            double credit = finalBalances.get(creditorId);
            double amountToPay = Math.min(debt, credit);//en küçük meblağı seçti

            String debtorName = "";
            String creditorName = "";
            for (User u : room.getUsers()) {
                if (u.getId().equals(debtorId)) debtorName = u.getName();
                if (u.getId().equals(creditorId)) creditorName = u.getName();
            }

            String instruction = debtorName + ", " + creditorName + " adlı kişiye " + amountToPay + " TL ödemeli.";
            transferInstructions.add(instruction);

            finalBalances.put(debtorId, finalBalances.get(debtorId) + amountToPay);
            finalBalances.put(creditorId, finalBalances.get(creditorId) - amountToPay);

            if (Math.abs(finalBalances.get(debtorId)) < 0.01) d++;
            if (finalBalances.get(creditorId) < 0.01) c++;
        }

        return transferInstructions;
    }

    private ExpenseResponse toResponse(Expense expense) {
        return new ExpenseResponse(
                expense.getId(),
                expense.getProductName(),
                expense.getDescription(),
                expense.getAmount(),
                expense.getDate(),
                expense.getUser().getName()
        );
    }
}
package com.example.demo.controller;

import com.example.demo.dto.RoomRequest;
import com.example.demo.dto.RoomResponse;
import com.example.demo.dto.UserResponse;
import com.example.demo.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    @PostMapping
    public ResponseEntity<RoomResponse> createRoom(@RequestBody RoomRequest request) {
        return ResponseEntity.ok(roomService.createRoom(request));
    }

    @PostMapping("/{roomId}/users/{userId}")
    public ResponseEntity<RoomResponse> addUserToRoom(
            @PathVariable Long roomId,
            @PathVariable Long userId) {
        return ResponseEntity.ok(roomService.addUserToRoom(roomId, userId));
    }

    @DeleteMapping("/{roomId}/users/{userId}")
    public ResponseEntity<RoomResponse> removeUserFromRoom(
            @PathVariable Long roomId,
            @PathVariable Long userId) {
        return ResponseEntity.ok(roomService.removeUserFromRoom(roomId, userId));
    }

    @GetMapping("/{roomId}/users")
    public ResponseEntity<List<UserResponse>> getUsersInRoom(@PathVariable Long roomId) {
        return ResponseEntity.ok(roomService.getUsersInRoom(roomId));
    }
}
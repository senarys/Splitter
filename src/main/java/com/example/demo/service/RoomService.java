package com.example.demo.service;

import com.example.demo.dto.RoomRequest;
import com.example.demo.dto.RoomResponse;
import com.example.demo.dto.UserResponse;
import com.example.demo.model.Room;
import com.example.demo.model.User;
import com.example.demo.repository.RoomRepository;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.example.demo.exception.RoomNotFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomService
{
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;

    public RoomResponse createRoom(RoomRequest request)
    {
        Room room = new Room();
        room.setRoomName(request.roomName());
        room.setRoomPassword(request.roomPassword()); // ilerde hashlicez

        Room saved = roomRepository.save(room);
        return new RoomResponse(saved.getId(), saved.getRoomName());
    }

    public Room getRoomOrThrow(Long roomId) {
        return roomRepository.findById(roomId)
                .orElseThrow(() -> new RoomNotFoundException("Oda bulunamadı!"));
    }
    public RoomResponse addUserToRoom(Long roomId, Long userId) {
        Room room = getRoomOrThrow(roomId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı!"));

        // zaten odadaysa tekrar eklemeyelim
        if (!room.getUsers().contains(user)) {
            room.getUsers().add(user);
            user.getRooms().add(room);
            roomRepository.save(room);
        }

        return new RoomResponse(room.getId(), room.getRoomName());
    }

    public RoomResponse removeUserFromRoom(Long roomId, Long userId) {
        Room room = getRoomOrThrow(roomId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı!"));

        room.getUsers().remove(user);
        user.getRooms().remove(room);
        roomRepository.save(room);

        return new RoomResponse(room.getId(), room.getRoomName());
    }

    public List<UserResponse> getUsersInRoom(Long roomId) {
        Room room = getRoomOrThrow(roomId);
        return room.getUsers()
                .stream()
                .map(u -> new UserResponse(u.getId(), u.getName(), u.getEmail()))
                .toList();
    }
}

package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import java.util.List;

@Entity
@Table(name = "rooms")
@Data
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String roomName;
    private String roomPassword;

    @ManyToMany(mappedBy = "rooms")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<User> users;
}
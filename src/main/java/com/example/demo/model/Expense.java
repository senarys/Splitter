package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import java.time.LocalDateTime;

@Entity
@Table(name = "expenses")
@Data
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String productName;
    private String description;
    private Double amount;
    private LocalDateTime date;
    private String filePath;

    @ManyToOne
    @JoinColumn(name = "user_id")//foreign_key
    @ToString.Exclude
    @EqualsAndHashCode.Exclude//stackoverflow önleyecek
    private User user;

    @ManyToOne
    @JoinColumn(name = "room_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Room room;
}
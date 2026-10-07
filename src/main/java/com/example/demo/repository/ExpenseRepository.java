package com.example.demo.repository;

import com.example.demo.model.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
//JPQL
    @Query("SELECT e FROM Expense e JOIN FETCH e.user WHERE e.room.id = :roomId")
    List<Expense> findByRoomId(@Param("roomId") Long roomId);
}
//e alias
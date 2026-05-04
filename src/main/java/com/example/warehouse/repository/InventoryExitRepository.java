package com.example.warehouse.repository;

import com.example.warehouse.model.InventoryExit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface InventoryExitRepository extends JpaRepository<InventoryExit, Long> {
    List<InventoryExit> findByDateBetween(LocalDate start, LocalDate end);
}

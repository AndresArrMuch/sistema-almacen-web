package com.example.warehouse.repository;

import com.example.warehouse.model.InventoryEntry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface InventoryEntryRepository extends JpaRepository<InventoryEntry, Long> {
    List<InventoryEntry> findByDateBetween(LocalDate start, LocalDate end);
}

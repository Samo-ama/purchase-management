package com.example.purchase.management.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.purchase.management.entity.Purchase;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
    List<Purchase> findAllByDateBetween(LocalDateTime start, LocalDateTime end);

}

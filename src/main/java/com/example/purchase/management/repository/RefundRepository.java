package com.example.purchase.management.repository;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

import com.example.purchase.management.entity.Refund;

public interface RefundRepository extends JpaRepository<Refund, Long> {
    List<Refund> findAllByDateBetween(LocalDateTime start, LocalDateTime end);




}

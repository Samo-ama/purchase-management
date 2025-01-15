package com.example.purchase.management.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.purchase.management.entity.Refund;

public interface RefundRepository extends JpaRepository<Refund, Long> {

}

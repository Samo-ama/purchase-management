package com.example.purchase.management.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.purchase.management.entity.Purchase;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {

}

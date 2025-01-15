package com.example.purchase.management.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.purchase.management.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

}

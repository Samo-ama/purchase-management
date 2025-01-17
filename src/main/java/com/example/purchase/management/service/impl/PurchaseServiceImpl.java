package com.example.purchase.management.service.impl;

import com.example.purchase.management.entity.Purchase;
import com.example.purchase.management.repository.PurchaseRepository;
import com.example.purchase.management.repository.CustomerRepository;
import com.example.purchase.management.repository.ProductRepository;
import com.example.purchase.management.service.PurchaseService;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PurchaseServiceImpl implements PurchaseService {

    private final PurchaseRepository purchaseRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;

    @Override
    public Purchase createPurchase(Purchase purchase) {
        // Validate and get complete customer
        var customer = customerRepository.findById(purchase.getCustomer().getId())
                .orElseThrow(() -> new EntityNotFoundException("Customer not found"));

        // Validate and get complete product
        var product = productRepository.findById(purchase.getProduct().getId())
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        // Validate amount
        if (purchase.getAmount() == null || purchase.getAmount() <= 0) {
            throw new IllegalArgumentException("Valid amount is required");
        }

        // Set complete objects
        purchase.setCustomer(customer);
        purchase.setProduct(product);

        return purchaseRepository.save(purchase);
    }

    @Override
    public List<Purchase> getAllPurchases() {
        return purchaseRepository.findAll();
    }

    @Override
    public List<Purchase> getYesterdayPurchases() {
        LocalDateTime start = LocalDate.now().minusDays(1).atStartOfDay();
        LocalDateTime end = LocalDate.now().atStartOfDay();
        return purchaseRepository.findAllByDateBetween(start, end);
    }
}
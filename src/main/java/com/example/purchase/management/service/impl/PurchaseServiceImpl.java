package com.example.purchase.management.service.impl;

import com.example.purchase.management.entity.Purchase;
import com.example.purchase.management.repository.PurchaseRepository;
import com.example.purchase.management.repository.CustomerRepository;
import com.example.purchase.management.repository.ProductRepository;
import com.example.purchase.management.service.PurchaseService;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PurchaseServiceImpl implements PurchaseService {

    private final PurchaseRepository purchaseRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;

    @Override
    public Purchase createPurchase(Purchase purchase) {
        // Validate customer exists
        customerRepository.findById(purchase.getCustomer().getId())
                .orElseThrow(() -> new EntityNotFoundException("Customer not found"));

        // Validate product exists
        productRepository.findById(purchase.getProduct().getId())
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        // Validate amount
        if (purchase.getAmount() == null || purchase.getAmount() <= 0) {
            throw new IllegalArgumentException("Valid amount is required");
        }

        return purchaseRepository.save(purchase);
    }

    @Override
    public List<Purchase> getAllPurchases() {
        return purchaseRepository.findAll();
    }
}
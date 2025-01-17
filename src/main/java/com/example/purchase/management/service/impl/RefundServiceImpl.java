package com.example.purchase.management.service.impl;

import com.example.purchase.management.entity.Purchase;
import com.example.purchase.management.entity.Refund;
import com.example.purchase.management.repository.RefundRepository;
import com.example.purchase.management.repository.PurchaseRepository;
import com.example.purchase.management.service.RefundService;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


@Service
@RequiredArgsConstructor
public class RefundServiceImpl implements RefundService {
    
    private final RefundRepository refundRepository;
    private final PurchaseRepository purchaseRepository;

    @Override
    public Refund createRefund(Refund refund) {
        // Validate and get complete purchase
        var purchase = purchaseRepository.findById(refund.getPurchase().getId())
            .orElseThrow(() -> new EntityNotFoundException("Purchase not found"));
            
        // Validate refund amount
        if (refund.getAmount() == null || refund.getAmount() <= 0 || 
            refund.getAmount() > purchase.getAmount()) {
            throw new IllegalArgumentException("Invalid refund amount");
        }
        
        // Set all details from purchase
        refund.setPurchase(purchase);  
        refund.setCustomer(purchase.getCustomer());
        refund.setProduct(purchase.getProduct());
        
        return refundRepository.save(refund);
    }

    @Override
    public List<Refund> getAllRefunds() {
        return refundRepository.findAll();
    }

    @Override
    public List<Refund> getYesterdayRefunds() {
        LocalDateTime start = LocalDate.now().minusDays(1).atStartOfDay();
        LocalDateTime end = LocalDate.now().atStartOfDay();
        return refundRepository.findAllByDateBetween(start, end);
    }
}

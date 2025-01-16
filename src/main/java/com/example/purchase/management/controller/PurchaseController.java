package com.example.purchase.management.controller;

import com.example.purchase.management.entity.Product;
import com.example.purchase.management.entity.Purchase;
import com.example.purchase.management.service.PurchaseService;
import com.example.purchase.management.service.impl.PurchaseServiceImpl;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/purchase")
@RequiredArgsConstructor
public class PurchaseController {

    private final PurchaseService purchaseService;
    
    //Creat Purchase
    @PostMapping
    public ResponseEntity<Purchase> createPurchase(@RequestBody Purchase purchase) {
        try {
            return ResponseEntity.ok(purchaseService.createPurchase(purchase));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    //Get all Purchase
    @GetMapping
    public ResponseEntity<List<Purchase>> getAllProducts() {
        return ResponseEntity.ok(purchaseService.getAllPurchases());
    }
}

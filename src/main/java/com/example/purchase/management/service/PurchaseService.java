package com.example.purchase.management.service;

import com.example.purchase.management.entity.Purchase;
import java.util.List;
import java.time.LocalDateTime;

public interface PurchaseService {

    Purchase createPurchase(Purchase purchase);
    List<Purchase> getAllPurchases();
    List<Purchase> getYesterdayPurchases();
    List<Purchase> getPurchasesBetween(LocalDateTime start, LocalDateTime end);


}

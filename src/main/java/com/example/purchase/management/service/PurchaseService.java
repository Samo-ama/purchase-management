package com.example.purchase.management.service;

import com.example.purchase.management.entity.Purchase;
import java.util.List;


public interface PurchaseService {

    Purchase createPurchase(Purchase purchase);
    List<Purchase> getAllPurchases();

}

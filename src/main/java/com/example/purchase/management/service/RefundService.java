package com.example.purchase.management.service;

import com.example.purchase.management.entity.Refund;
import java.util.List;

public interface RefundService {
    Refund createRefund(Refund refund);
    List<Refund> getAllRefunds();
    List<Refund> getYesterdayRefunds();
}

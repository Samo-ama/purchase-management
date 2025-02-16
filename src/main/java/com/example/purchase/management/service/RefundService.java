package com.example.purchase.management.service;

import com.example.purchase.management.entity.Refund;
import java.util.List;
import java.time.LocalDateTime;

public interface RefundService {
    Refund createRefund(Refund refund);
    List<Refund> getAllRefunds();
    List<Refund> getYesterdayRefunds();
    List<Refund> getRefundBetween(LocalDateTime start, LocalDateTime end);
}

package com.example.purchase.management.report;

import com.example.purchase.management.entity.Purchase;
import com.example.purchase.management.entity.Refund;
import java.util.List;

public interface ReportGenerator {
    String generateReport(List<Purchase> purchases, List<Refund> refunds);
}

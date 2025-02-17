package com.example.purchase.management.service.impl;

import com.example.purchase.management.report.ReportGenerator;
import com.example.purchase.management.service.PurchaseService;
import com.example.purchase.management.service.RefundService;
import com.example.purchase.management.service.ReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionReportService implements ReportService {

    private final ReportGenerator reportGenerator;
    private final PurchaseService purchaseService;
    private final RefundService refundService;

    @Override
    public String generateReport(LocalDateTime start, LocalDateTime end) {
        try {

            // Get yesterday's transactions
            var purchases = purchaseService.getPurchasesBetween(start, end);
            var refunds = refundService.getRefundBetween(start,end);

            String htmlReport = reportGenerator.generateReport(purchases, refunds);

            return htmlReport;
            
        } catch (Exception e) {
            log.error("Failed to generate and send report: ", e);
            throw new RuntimeException("Failed to generate and send report", e);
        }
    }

   /*  @Override
    public void generateAndSendReport() {
        try {

            // Get yesterday's transactions
            var purchases = purchaseService.getYesterdayPurchases();
            var refunds = refundService.getYesterdayRefunds();

            String htmlReport = reportGenerator.generateReport(purchases, refunds);

            emailService.send(
                    "Daily Transactions Report - " + LocalDate.now().minusDays(1),
                    htmlReport);
            log.info("Report generated and sent successfully");

        } catch (Exception e) {
            log.error("Failed to generate and send report: ", e);
            throw new RuntimeException("Failed to generate and send report", e);
        }
    } */

    
}
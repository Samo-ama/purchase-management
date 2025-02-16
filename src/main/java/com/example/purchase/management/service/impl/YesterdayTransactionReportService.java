package com.example.purchase.management.service.impl;

import com.example.purchase.management.repository.PurchaseRepository;
import com.example.purchase.management.repository.RefundRepository;
import com.example.purchase.management.service.SenderService;
import com.example.purchase.management.service.PurchaseService;
import com.example.purchase.management.service.RefundService;
import com.example.purchase.management.service.ReportService;
import com.example.purchase.management.report.ReportGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.LocalDateTime;

//import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class YesterdayTransactionReportService implements ReportService {

   
    private final SenderService emailService;
    private final ReportGenerator reportGenerator;
    private final PurchaseService purchaseService;
    private final RefundService refundService;
    LocalDateTime start;
    LocalDateTime end ;

    @Override
    public String generateReport() {
        try {

             start = LocalDate.now().minusDays(1).atStartOfDay();
             end = LocalDate.now().atStartOfDay();

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
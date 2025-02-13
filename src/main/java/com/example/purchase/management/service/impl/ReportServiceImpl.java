package com.example.purchase.management.service.impl;

import com.example.purchase.management.repository.PurchaseRepository;
import com.example.purchase.management.repository.RefundRepository;
import com.example.purchase.management.service.EmailService;
import com.example.purchase.management.service.PurchaseService;
import com.example.purchase.management.service.RefundService;
import com.example.purchase.management.service.ReportService;
import com.example.purchase.management.report.ReportGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;

//import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

   
    private final EmailService emailService;
    private final ReportGenerator reportGenerator;
    private final PurchaseService purchaseService;
    private final RefundService refundService;

    @Override
    public void generateAndSendReport() {
        try {

            // Get yesterday's transactions
            var purchases = purchaseService.getYesterdayPurchases();
            var refunds = refundService.getYesterdayRefunds();

            String htmlReport = reportGenerator.generateReport(purchases, refunds);

            emailService.sendHtmlEmail(
                    "Daily Transactions Report - " + LocalDate.now().minusDays(1),
                    htmlReport);
            log.info("Report generated and sent successfully");

        } catch (Exception e) {
            log.error("Failed to generate and send report: ", e);
            throw new RuntimeException("Failed to generate and send report", e);
        }
    }

    
}
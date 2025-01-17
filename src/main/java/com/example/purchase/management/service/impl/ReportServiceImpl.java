package com.example.purchase.management.service.impl;

import com.example.purchase.management.repository.PurchaseRepository;
import com.example.purchase.management.repository.RefundRepository;
import com.example.purchase.management.service.EmailService;
import com.example.purchase.management.service.ReportService;
import com.example.purchase.management.report.ReportGenerator;
import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final PurchaseRepository purchaseRepository;
    private final RefundRepository refundRepository;
    private final EmailService emailService;
    private final ReportGenerator reportGenerator;

    
     // Temporarily hardcoded email
     private final String reportEmail = "Ebtisamalaama@gmail.com";
    

    @Override
    public void generateAndSendReport() {
        var purchases = purchaseRepository.findAll();
        var refunds = refundRepository.findAll();

        String htmlReport = reportGenerator.generateReport(purchases, refunds);
        
        emailService.sendHtmlEmail(
            reportEmail,
            "Transactions Report - Test",
            htmlReport
        );
    }
}
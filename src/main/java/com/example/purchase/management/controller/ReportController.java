package com.example.purchase.management.controller;

import com.example.purchase.management.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/report")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @PostMapping("/send")
    public ResponseEntity<String> sendReport() {
        try {
            reportService.generateAndSendReport();
            return ResponseEntity.ok("Report sent successfully");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Failed to send report: " + e.getMessage());
        }
    }
}

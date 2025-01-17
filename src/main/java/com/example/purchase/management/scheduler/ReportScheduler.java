package com.example.purchase.management.scheduler;

import com.example.purchase.management.service.ReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReportScheduler {

    private final ReportService reportService;

     
    //Scheduled(cron = "0 * * * * *") // Runs every minute, For testing
    @Scheduled(cron = "0 0 1 * * *")  // Runs at 1 AM every day
    public void sendDailyReport() {
        log.info("Starting daily report generation...");
        try {
            reportService.generateAndSendReport();
            log.info("Daily report sent successfully");
        } catch (Exception e) {
            log.error("Failed to send daily report: ", e);
        }
    }
}

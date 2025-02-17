package com.example.purchase.management.scheduler;

import com.example.purchase.management.service.ReportService;
import com.example.purchase.management.service.SenderService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReportScheduler {

    private final ReportService reportService;
    private final SenderService emailService;

    private LocalDate yesterdayDate = LocalDate.now().minusDays(1);
    private LocalDateTime start = yesterdayDate.atStartOfDay();
    private LocalDateTime end = LocalDate.now().atStartOfDay();

    //Scheduled(cron = "0 * * * * *") // Runs every minute, For testing
    @Scheduled(cron = "0 0 1 * * *")  // Runs at 1 AM every day
    public void sendScheduledReport() {
        log.info("Starting daily report generation...");
        try {

           String YesterdayTransactionReport = reportService.generateReport(start, end);

            emailService.send(
            "Daily Transactions Report - " + yesterdayDate,
            YesterdayTransactionReport);
            log.info("Daily report sent successfully");
        } catch (Exception e) {
            log.error("Failed to send daily report: ", e);
        }
    }

   /*  @Scheduled(cron = "0 0 1 * * *")  // Runs at 1 AM every day
    public void sendDailyReport() {
        log.info("Starting daily report generation...");
        try {
            reportService.generateReport();
            log.info("Daily report sent successfully");
        } catch (Exception e) {
            log.error("Failed to send daily report: ", e);
        }
    } */
}

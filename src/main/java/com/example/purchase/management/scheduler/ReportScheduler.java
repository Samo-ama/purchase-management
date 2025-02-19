package com.example.purchase.management.scheduler;

import com.example.purchase.management.service.ReportService;
import com.example.purchase.management.service.SenderService;
import com.example.purchase.management.service.DateRangeProvider;

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
    private final SenderService emailSender;
    private final DateRangeProvider dateProvider;

    @Scheduled(cron = "0 0 1 * * *") // Runs at 1 AM every day
    public void sendScheduledReport() {
        log.info("Starting daily report generation...");
        try {

            // Generate report for yesterday's purchases and refunds
            var dateRange = dateProvider.getYesterdayRange();
            String TransactionReport = reportService.generateReport(dateRange.getStart(), dateRange.getEnd());

            emailSender.send(
                    "Daily Transactions Report - " + dateRange.getStart(),
                    TransactionReport);
            log.info("Daily report sent successfully");
        } catch (Exception e) {
            log.error("Failed to send daily report: ", e);
        }
    }

}

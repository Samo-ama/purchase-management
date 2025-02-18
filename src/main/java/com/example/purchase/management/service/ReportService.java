package com.example.purchase.management.service;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface ReportService {
    String generateReport(LocalDateTime start, LocalDateTime end);

}

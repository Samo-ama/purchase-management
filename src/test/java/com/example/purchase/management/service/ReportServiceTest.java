package com.example.purchase.management.service;

import com.example.purchase.management.entity.Purchase;
import com.example.purchase.management.entity.Refund;
import com.example.purchase.management.service.impl.YesterdayTransactionReportService;
import com.example.purchase.management.report.ReportGenerator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class ReportServiceTest {

    @InjectMocks
    private YesterdayTransactionReportService reportService;
    
    @Mock
    private PurchaseService purchaseService;

    @Mock
    private RefundService refundService;

    @Mock
    private ReportGenerator reportGenerator;

    @Mock
    private SenderService emailService;

    private List<Purchase> testPurchases;
    private List<Refund> testRefunds;
    private String testHtmlReport;

    /**
     * Set up test data before each test
     */
    @BeforeEach
    void setUp() {
        // Create test data

        testPurchases = Arrays.asList(new Purchase());
        testRefunds = Arrays.asList(new Refund());
        testHtmlReport = "<html>Test Report</html>";
        
    }

    /**
     * Test successful report generation and sending
     * Expected: Report should be generated and sent successfully
     */
    @Test
    void generateAndSendReport_ShouldSucceed() {
        // Arrange

        when(purchaseService.getYesterdayPurchases()).thenReturn(testPurchases);
        when(refundService.getYesterdayRefunds()).thenReturn(testRefunds);
        when(reportGenerator.generateReport(testPurchases, testRefunds)).thenReturn(testHtmlReport);

        reportService.generateReport();

         // Assert
         verify(purchaseService).getYesterdayPurchases();
         verify(refundService).getYesterdayRefunds();
         verify(reportGenerator).generateReport(testPurchases, testRefunds);
         verify(emailService).send(
             eq("Daily Transactions Report - " + LocalDate.now().minusDays(1)),
             eq(testHtmlReport) );
    }

    /**
     * Test report generation when purchase service fails
     * Expected: Should throw RuntimeException
     */
    @Test
    void generateAndSendReport_WhenPurchaseServiceFails_ShouldThrowException() {
        // Arrange
        when(purchaseService.getYesterdayPurchases())
            .thenThrow(new RuntimeException("Purchase service failed"));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
            () -> reportService.generateReport());

        assertEquals("Failed to generate and send report", exception.getMessage());
        verify(emailService, never()).send(anyString(), anyString());
    }

    /**
     * Test report generation when email service fails
     * Expected: Should throw RuntimeException
     */
    @Test
    void generateAndSendReport_WhenEmailServiceFails_ShouldThrowException() {
        // Arrange
        when(purchaseService.getYesterdayPurchases()).thenReturn(testPurchases);
        when(refundService.getYesterdayRefunds()).thenReturn(testRefunds);
        when(reportGenerator.generateReport(testPurchases, testRefunds)).thenReturn(testHtmlReport);
        doThrow(new RuntimeException("Email service failed"))
            .when(emailService).send(anyString(), anyString());

        // Act
        RuntimeException exception = assertThrows(RuntimeException.class,
            () -> reportService.generateReport());

        assertEquals("Failed to generate and send report", exception.getMessage());
    }



    



}

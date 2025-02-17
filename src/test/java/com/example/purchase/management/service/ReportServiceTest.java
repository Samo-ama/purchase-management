package com.example.purchase.management.service;

import com.example.purchase.management.entity.Purchase;
import com.example.purchase.management.entity.Refund;
import com.example.purchase.management.exception.ContentSizeExceededException;
import com.example.purchase.management.service.impl.TransactionReportService;
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
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class ReportServiceTest {

    @InjectMocks
    private TransactionReportService reportService;
    
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

    private LocalDateTime start;
    private LocalDateTime end;

    /**
     * Set up test data before each test
     */
    @BeforeEach
    void setUp() {
        // Create test data
        testPurchases = Arrays.asList(new Purchase());
        testRefunds = Arrays.asList(new Refund());
        testHtmlReport = "<html>Test Report</html>";

        LocalDateTime start = LocalDate.now().minusDays(1).atStartOfDay();
        LocalDateTime end = LocalDate.now().atStartOfDay();

    }

    /**
     * Test successful report generation and sending
     * Expected: Report should be generated and sent successfully
     */
    @Test
    void generateReport_ShouldSucceed() {
        // Arrange
        when(purchaseService.getPurchasesBetween(start, end)).thenReturn(testPurchases);
        when(refundService.getRefundBetween(start, end)).thenReturn(testRefunds);
        when(reportGenerator.generateReport(testPurchases, testRefunds)).thenReturn(testHtmlReport);

        reportService.generateReport(start, end);

         // Assert
         verify(purchaseService).getPurchasesBetween(start, end);
         verify(refundService).getRefundBetween(start, end);
         verify(reportGenerator).generateReport(testPurchases, testRefunds);
    }

    /**
     * Test report generation when purchase service fails
     * Expected: Should throw RuntimeException
     */
    @Test
    void generateReport_WhenPurchaseServiceFails_ShouldThrowException() throws ContentSizeExceededException {
        // Arrange
        when(purchaseService.getPurchasesBetween(start, end))
            .thenThrow(new RuntimeException("Purchase service failed"));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
            () -> reportService.generateReport(start, end));

        assertEquals("Failed to generate and send report", exception.getMessage());
        verify(emailService, never()).send(anyString(), anyString());
    }






}

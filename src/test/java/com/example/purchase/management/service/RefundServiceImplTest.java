package com.example.purchase.management.service;

import com.example.purchase.management.entity.Purchase;
import com.example.purchase.management.entity.Refund;
import com.example.purchase.management.repository.PurchaseRepository;
import com.example.purchase.management.repository.RefundRepository;
import com.example.purchase.management.service.impl.RefundServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RefundServiceImplTest {

    @InjectMocks
    private RefundServiceImpl refundService;

    @Mock
    private RefundRepository refundRepository;

    @Mock
    private PurchaseRepository purchaseRepository;

    private Refund validRefund;
    private Purchase validPurchase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        validPurchase = new Purchase();
        validPurchase.setId(1L);
        validPurchase.setAmount(1000.00);

        validRefund = new Refund();
        validRefund.setPurchase(validPurchase);
        validRefund.setAmount(500.00);
    }

    @Test
    public void createRefund_WithValidData_ShouldSaveRefund() {
        // Arrange
        when(purchaseRepository.findById(validPurchase.getId())).thenReturn(Optional.of(validPurchase));
        when(refundRepository.save(validRefund)).thenReturn(validRefund);

        // Act
        Refund actualRefund = refundService.createRefund(validRefund);

        // Assert
        assertNotNull(actualRefund);
        assertEquals(validRefund.getAmount(), actualRefund.getAmount());
        verify(refundRepository, times(1)).save(validRefund);
    }

    @Test
    public void createRefund_WithNonExistentPurchase_ShouldThrowEntityNotFoundException() {
        // Arrange
        when(purchaseRepository.findById(validPurchase.getId())).thenReturn(Optional.empty());

        // Act & Assert
        var exp = assertThrows(
                EntityNotFoundException.class,
                () -> refundService.createRefund(validRefund)
        );
        assertEquals("Purchase not found", exp.getMessage());
        verify(refundRepository, never()).save(validRefund);
    }

    @Test
    public void createRefund_WithInvalidAmount_ShouldThrowIllegalArgumentException() {
        // Arrange
        validRefund.setAmount(-100.00);
        when(purchaseRepository.findById(validPurchase.getId())).thenReturn(Optional.of(validPurchase));

        // Act & Assert
        var exp = assertThrows(
                IllegalArgumentException.class,
                () -> refundService.createRefund(validRefund)
        );
        assertEquals("Invalid refund amount", exp.getMessage());
        verify(refundRepository, never()).save(validRefund);
    }

    @Test
    public void createRefund_WithExcessiveAmount_ShouldThrowIllegalArgumentException() {
        // Arrange
        validRefund.setAmount(1500.00);
        when(purchaseRepository.findById(validPurchase.getId())).thenReturn(Optional.of(validPurchase));

        // Act & Assert
        var exp = assertThrows(
                IllegalArgumentException.class,
                () -> refundService.createRefund(validRefund)
        );
        assertEquals("Invalid refund amount", exp.getMessage());
        verify(refundRepository, never()).save(validRefund);
    }

    @Test
    public void getAllRefunds_ShouldReturnListOfRefunds() {
        // Arrange
        List<Refund> refunds = new ArrayList<>();
        refunds.add(validRefund);
        Refund anotherRefund = new Refund();
        anotherRefund.setAmount(200.00);
        refunds.add(anotherRefund);

        when(refundRepository.findAll()).thenReturn(refunds);

        // Act
        List<Refund> returnedList = refundService.getAllRefunds();

        // Assert
        assertEquals(refunds.size(), returnedList.size());
        assertEquals(refunds, returnedList);
    }

    @Test
    public void getAllRefunds_NoRefunds_ShouldReturnEmptyList() {
        // Arrange
        List<Refund> refunds = new ArrayList<>();
        when(refundRepository.findAll()).thenReturn(refunds);

        // Act
        List<Refund> returnedList = refundService.getAllRefunds();

        // Assert
        assertTrue(returnedList.isEmpty());
    }

    @Test
    public void getYesterdayRefunds_ShouldReturnListOfYesterdayRefunds() {
        // Arrange
        LocalDateTime yesterdayStart = LocalDate.now().minusDays(1).atStartOfDay();
        LocalDateTime yesterdayEnd = LocalDate.now().atStartOfDay();
        List<Refund> refunds = new ArrayList<>();
        Refund yesterdayRefund = new Refund();
        yesterdayRefund.setAmount(300.00);
        refunds.add(yesterdayRefund);

        when(refundRepository.findAllByDateBetween(yesterdayStart, yesterdayEnd)).thenReturn(refunds);

        // Act
        List<Refund> returnedList = refundService.getYesterdayRefunds();

        // Assert
        assertEquals(refunds.size(), returnedList.size());
        assertEquals(refunds, returnedList);
    }
}

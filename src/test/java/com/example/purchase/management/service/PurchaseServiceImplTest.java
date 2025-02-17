package com.example.purchase.management.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.purchase.management.entity.Purchase;
import com.example.purchase.management.entity.Customer;
import com.example.purchase.management.entity.Product;
import com.example.purchase.management.repository.PurchaseRepository;
import com.example.purchase.management.repository.CustomerRepository;
import com.example.purchase.management.repository.ProductRepository;
import com.example.purchase.management.service.impl.PurchaseServiceImpl;
import jakarta.persistence.EntityNotFoundException;
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
import java.util.Optional;
import java.util.ArrayList;

@ExtendWith(MockitoExtension.class)
public class PurchaseServiceImplTest {

    @Mock
    private PurchaseRepository purchaseRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private PurchaseServiceImpl purchaseService;

    private Purchase testPurchase;
    private Customer testCustomer;
    private Product testProduct;

    /**
     * Set up test data
     */
    @BeforeEach
    void setUp() {
        // Setup test customer
        testCustomer = new Customer();
        testCustomer.setId(1L);
        testCustomer.setFirstName("ahmad");
        testCustomer.setLastName("Safi");
        testCustomer.setPhone("0933336666");

        // Setup test product
        testProduct = new Product();
        testProduct.setId(1L);
        testProduct.setName("Test Product");
        testProduct.setPrice(100.0);

        // Setup test purchase
        testPurchase = new Purchase();
        testPurchase.setId(1L);
        testPurchase.setCustomer(testCustomer);
        testPurchase.setProduct(testProduct);
        testPurchase.setAmount(2.0);
        testPurchase.setDate(LocalDateTime.now());
    }

    // CREATE PURCHASE TESTS

    /**
     * Test creating a purchase with valid data
     * Expected: Purchase should be created successfully
     */
    @Test
    void createPurchase_WithValidData_ShouldCreatePurchase() {
        // Arrange
        when(customerRepository.findById(1L)).thenReturn(Optional.of(testCustomer));
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        when(purchaseRepository.save(any(Purchase.class))).thenReturn(testPurchase);

        // Act
        Purchase created = purchaseService.createPurchase(testPurchase);

        // Assert
        assertNotNull(created);
        assertEquals(2, created.getAmount());
        assertEquals(testCustomer.getId(), created.getCustomer().getId());
        assertEquals(testProduct.getId(), created.getProduct().getId());
        verify(purchaseRepository, times(1)).save(any(Purchase.class));
    }

    /**
     * Test creating a purchase with non-existent customer
     * Expected: Should throw EntityNotFoundException
     */
    @Test
    void createPurchase_WithNonExistentCustomer_ShouldThrowException() {
        // Arrange
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> purchaseService.createPurchase(testPurchase));

        assertEquals("Customer not found", exception.getMessage());
        verify(purchaseRepository, never()).save(any(Purchase.class));
    }

    /**
     * Test creating a purchase with non-existent product
     * Expected: Should throw EntityNotFoundException
     */
    @Test
    void createPurchase_WithNonExistentProduct_ShouldThrowException() {
        // Arrange
        when(customerRepository.findById(1L)).thenReturn(Optional.of(testCustomer));
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> purchaseService.createPurchase(testPurchase));

        assertEquals("Product not found", exception.getMessage());
        verify(purchaseRepository, never()).save(any(Purchase.class));
    }

    /**
     * Test creating a purchase with invalid amount
     * Expected: Should throw IllegalArgumentException
     */
    @Test
    void createPurchase_WithInvalidAmount_ShouldThrowException() {
        // Arrange
        testPurchase.setAmount(0.0);
        when(customerRepository.findById(1L)).thenReturn(Optional.of(testCustomer));
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> purchaseService.createPurchase(testPurchase));

        assertEquals("Valid amount is required", exception.getMessage());
        verify(purchaseRepository, never()).save(any(Purchase.class));
    }

    // GET ALL PURCHASES TESTS

    /**
     * Test retrieving all purchases
     * Expected: Should return list of all purchases
     */
    @Test
    void getAllPurchases_ShouldReturnAllPurchases() {
        // Arrange
        List<Purchase> purchaseList = new ArrayList<>();
        purchaseList.add(testPurchase);

        when(purchaseRepository.findAll()).thenReturn(purchaseList);

        // Act
        List<Purchase> result = purchaseService.getAllPurchases();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(purchaseRepository, times(1)).findAll();
    }

    /**
     * Test retrieving all purchases when no purchases exist
     * Expected: Should return empty list
     */
    @Test
    void getAllPurchases_WhenNoPurchases_ShouldReturnEmptyList() {
        // Arrange
        when(purchaseRepository.findAll()).thenReturn(Arrays.asList());

        // Act
        List<Purchase> result = purchaseService.getAllPurchases();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(purchaseRepository, times(1)).findAll();
    }

    @Test
    void getAllPurchases_WhenLargeNumberOfPurchases_ShouldReturnListOfPurchases() {
        // Arrange
        int numberOfPurchases = 1000000;
        List<Purchase> purchases = new ArrayList<>();

        for (int i = 0; i < numberOfPurchases; i++) {
            purchases.add(new Purchase());
        }

        // Mock
        when(purchaseRepository.findAll()).thenReturn(purchases);

        // Act
        List<Purchase> result = purchaseService.getAllPurchases();

        // Assert
        assertNotNull(result);
        assertEquals(numberOfPurchases, result.size());
        verify(purchaseRepository, times(1)).findAll();
    }


    /**
     * Test retrieving yesterday's purchases
     * Expected: Should return list of yesterday's purchases
     */
    @Test
    void getYesterdayPurchases_ShouldReturnYesterdayPurchases() {
        // Arrange
        LocalDateTime start = LocalDate.now().minusDays(1).atStartOfDay();
        LocalDateTime end = LocalDate.now().atStartOfDay();
        List<Purchase> purchaseList = Arrays.asList(testPurchase);

        when(purchaseRepository.findAllByDateBetween(start, end)).thenReturn(purchaseList);

        // Act
        List<Purchase> result = purchaseService.getYesterdayPurchases();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(purchaseRepository, times(1)).findAllByDateBetween(start, end);
    }

    /**
     * Test retrieving yesterday's purchases when no purchases exist
     * Expected: Should return empty list
     */
    @Test
    void getYesterdayPurchases_WhenNoPurchases_ShouldReturnEmptyList() {
        // Arrange
        when(purchaseRepository.findAllByDateBetween(any(), any())).thenReturn(Arrays.asList());

        // Act
        List<Purchase> result = purchaseService.getYesterdayPurchases();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(purchaseRepository, times(1)).findAllByDateBetween(any(), any());
    }

}

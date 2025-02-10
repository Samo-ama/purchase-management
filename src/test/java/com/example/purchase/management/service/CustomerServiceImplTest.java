package com.example.purchase.management.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.purchase.management.entity.Customer;
import com.example.purchase.management.repository.CustomerRepository;
import com.example.purchase.management.service.impl.CustomerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerServiceImpl customerService;

    private Customer testCustomer;

    /**
     * Set up test data before each test
     */
    @BeforeEach
    void setUp() {
        testCustomer = new Customer();
        testCustomer.setId(1L);
        testCustomer.setFirstName("Rami");
        testCustomer.setLastName("Ahmad");
        testCustomer.setPhone("0933333333");
    }

    // CREATE CUSTOMER TESTS
    /**
     * Test creating a customer with valid data
     * Expected: Customer should be created successfully
     */

    @Test
    void createCustomer_WithValidData_ShouldCreateCustomer() {
        // Arrange
        when(customerRepository.save(any(Customer.class))).thenReturn(testCustomer);

        // Act
        Customer created = customerService.createCustomer(testCustomer);

        // Assert
        assertNotNull(created);
        assertEquals("Rami", created.getFirstName());
        assertEquals("Ahmad", created.getLastName());
        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    /**
     * Test creating a customer with null first name
     * Expected: Should throw IllegalArgumentException
     */
    @Test
    void createCustomer_WithNullFirstName_ShouldThrowException() {

        // Arrange
        testCustomer.setFirstName(null);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> customerService.createCustomer(testCustomer));
        assertEquals("First name is required", exception.getMessage());
        verify(customerRepository, never()).save(any(Customer.class));
    }

    /**
     * Test creating a customer with empty first name
     * Expected: Should throw IllegalArgumentException
     */
    @Test
    void createCustomer_WithEmptyFirstName_ShouldThrowException() {
        // Arrange
        testCustomer.setFirstName("");

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> customerService.createCustomer(testCustomer));
        assertEquals("First name is required", exception.getMessage());
        verify(customerRepository, never()).save(any(Customer.class));
    }

    // UPDATE CUSTOMER TESTS

    /**
     * Test updating a customer with valid data
     * Expected: Customer should be updated successfully
     */
    @Test
    void updateCustomer_WithValidData_ShouldUpdateCustomer() {
        // Arrange
        when(customerRepository.findById(1L)).thenReturn(Optional.of(testCustomer));
        when(customerRepository.save(any(Customer.class))).thenReturn(testCustomer);

        Customer updateData = new Customer();
        updateData.setFirstName("Rami");
        updateData.setLastName("ama");
        updateData.setPhone("0933333333");

        // Act
        Customer updated = customerService.updateCustomer(1L, updateData);

        // Assert
        assertNotNull(updated);
        assertEquals("ama", updated.getLastName());
        verify(customerRepository, times(1)).findById(1L);
        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    /**
     * Test updating a non-existent customer
     * Expected: Should throw EntityNotFoundException
     */

    @Test
    void updateCustomer_WithNonExistentId_ShouldThrowException() {
        // Arrange
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> customerService.updateCustomer(1L, testCustomer));

        assertEquals("Customer not found", exception.getMessage());
        verify(customerRepository, times(1)).findById(1L);
        verify(customerRepository, never()).save(any(Customer.class));
    }

}

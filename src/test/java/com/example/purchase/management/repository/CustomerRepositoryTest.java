package com.example.purchase.management.repository;

import com.example.purchase.management.entity.Customer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.junit.jupiter.api.BeforeEach;


import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;

@DataJpaTest
public class CustomerRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CustomerRepository customerRepository;

    private Customer testCustomer;
    private Customer testCustomer2;
    private Customer savedCustomer;


    
    @BeforeEach
    void setUp() {
        testCustomer = new Customer();
        testCustomer.setFirstName("Ahmad");
        testCustomer.setLastName("Safi");
        testCustomer.setPhone("0933333333");

        testCustomer2 = new Customer();
        testCustomer2.setFirstName("Rami");
        testCustomer2.setLastName("Rami");
        testCustomer2.setPhone("0933333366");

        
    }

    @Test
    void save_ShouldSaveCustomerWithGeneratedId() {
        // Act
        Customer savedCustomer = customerRepository.save(testCustomer);

        // Assert
        assertNotNull(savedCustomer.getId());
        assertEquals(testCustomer.getFirstName(), savedCustomer.getFirstName());
        assertEquals(testCustomer.getLastName(), savedCustomer.getLastName());
    }

    /**
     * Test finding customer by ID
     * Expected: Should return customer when exists
     */
    @Test
    void findById_WhenCustomerExists_ShouldReturnCustomer() {
        // Arrange
        Customer savedCustomer = entityManager.persist(testCustomer);
        entityManager.flush();

        // Act
        Optional<Customer> found = customerRepository.findById(savedCustomer.getId());

        // Assert
        assertTrue(found.isPresent());
        assertEquals(savedCustomer.getFirstName(), found.get().getFirstName());
    }

     /**
     * Test finding customer by non-existent ID
     * Expected: Should return empty 
     */
    @Test
    void findById_WhenCustomerDoesNotExist_ShouldReturnEmpty() {
        // Act
        Optional<Customer> found = customerRepository.findById(99L);

        // Assert
        assertTrue(found.isEmpty());
    }

    /**
     * Test finding all customers
     * Expected: Should return list of all customers
     */
    @Test
    void findAll_ShouldReturnAllCustomers() {
        // Arrange
       
        entityManager.persist(testCustomer);
        entityManager.persist(testCustomer2);
        entityManager.flush();

        // Act
        List<Customer> customers = customerRepository.findAll();

        // Assert
        assertEquals(2, customers.size());
    }

     /**
     * Test finding customers when repository is empty
     * Expected: Should return empty list
     */
    @Test
    void findAll_WhenRepositoryEmpty_ShouldReturnEmptyList() {
        // Act
        List<Customer> customers = customerRepository.findAll();

        // Assert
        assertTrue(customers.isEmpty());
    }

    /**
     * Test deleting a customer
     * Expected: Customer should be deleted
     */
    @Test
    void delete_ShouldRemoveCustomer() {
        // Arrange
        Customer savedCustomer = entityManager.persist(testCustomer);
        entityManager.flush();

        customerRepository.deleteById(savedCustomer.getId());
        Optional<Customer> found = customerRepository.findById(savedCustomer.getId());

        assertTrue(found.isEmpty());

      
    }

    @Test
    void update_ShouldUpdateCustomerDetails() {
        // Arrange
        Customer savedCustomer = entityManager.persist(testCustomer);
        entityManager.flush();

        savedCustomer.setFirstName("Ayman");
        Customer updatedCustomer = customerRepository.save(savedCustomer);

        assertEquals("Ayman", updatedCustomer.getFirstName());
        assertEquals(savedCustomer.getId(), updatedCustomer.getId());



    
    }


}





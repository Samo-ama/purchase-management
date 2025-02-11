package com.example.purchase.management.controller;

import com.example.purchase.management.entity.Customer;
import com.example.purchase.management.service.CustomerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import java.util.Arrays;
import java.util.List;
import static org.hamcrest.Matchers.hasSize;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CustomerController.class)
@AutoConfigureMockMvc(addFilters = false) // disable security for tests
public class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc; // Mocks HTTP requests

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CustomerService customerService;

    private Customer testCustomer;

    @BeforeEach
    void setUp() {

        testCustomer = new Customer();
        testCustomer.setId(1L);
        testCustomer.setFirstName("Ahmad");
        testCustomer.setLastName("safi");
        testCustomer.setPhone("0933333333");

    }

    /**
     * Test creating a customer with valid data
     * Expected: Should return 200 OK and created customer
     */
    @Test
    void createCustomer_WithValidData_ShouldReturnCreated() throws Exception {

        when(customerService.createCustomer(any(Customer.class))).thenReturn(testCustomer);

        mockMvc.perform(post("/customer")

                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testCustomer)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("Ahmad"))
                .andExpect(jsonPath("$.lastName").value("safi"));

        verify(customerService, times(1)).createCustomer(any(Customer.class));

    }

    /**
     * Test creating a customer with invalid data
     * Expected: Should return 400 Bad Request
     */
    @Test
    void createCustomer_WithInvalidData_ShouldReturnBadRequest() throws Exception {
        when(customerService.createCustomer(any(Customer.class)))
                .thenThrow(new IllegalArgumentException("Invalid data"));

        mockMvc.perform(post("/customer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testCustomer)))
                .andExpect(status().isBadRequest());

        verify(customerService, times(1)).createCustomer(any(Customer.class));
    }

    /**
     * Test getting all customers
     * Expected: Should return 200 OK and list of customers
     */
    @Test
    void getAllCustomers_ShouldReturnList() throws Exception {
        List<Customer> customers = Arrays.asList(testCustomer);
        when(customerService.getAllCustomers()).thenReturn(customers);

        mockMvc.perform(get("/customer"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].firstName").value("Ahmad"))
                .andExpect(jsonPath("$[0].lastName").value("safi"));

        verify(customerService, times(1)).getAllCustomers();
    }

    /**
     * Test getting all customers when none exist
     * Expected: Should return 200 OK and empty list
     */
    @Test
    void getAllCustomers_WhenEmpty_ShouldReturnEmptyList() throws Exception {
        // Arrange
        when(customerService.getAllCustomers()).thenReturn(Arrays.asList());


        mockMvc.perform(get("/customer"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)))
                .andExpect(jsonPath("$").isArray()); 

        verify(customerService, times(1)).getAllCustomers();
    }

}

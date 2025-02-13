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

import java.util.Arrays;
import java.util.List;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.security.test.context.support.WithMockUser;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

//@AutoConfigureMockMvc(addFilters = false) // disable security for tests
@WebMvcTest(CustomerController.class)
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
                .with(csrf())
                .with(httpBasic("test", "test"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testCustomer))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value(testCustomer.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(testCustomer.getLastName()));

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
                .with(csrf())
                .with(httpBasic("test", "test"))
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

        mockMvc.perform(get("/customer")
               .with(httpBasic("test", "test")))
                
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].firstName").value(testCustomer.getFirstName()))
                .andExpect(jsonPath("$[0].lastName").value(testCustomer.getLastName()));

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

        mockMvc.perform(get("/customer")
                .with(httpBasic("test", "test")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)))
                .andExpect(jsonPath("$").isArray());

        verify(customerService, times(1)).getAllCustomers();
    }

    /**
     * Test updating a customer with valid data
     * Expected: Should return 200 OK and updated customer
     */
    @Test
    void updateCustomer_WithValidData_ShouldReturnUpdated() throws Exception {
        when(customerService.updateCustomer(eq(1L), any(Customer.class))).thenReturn(testCustomer);

        mockMvc.perform(put("/customer/1")
                .with(csrf())
                .with(httpBasic("test", "test"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testCustomer)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value(testCustomer.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(testCustomer.getLastName()));

        verify(customerService, times(1)).updateCustomer(eq(1L), any(Customer.class));
    }

    /**
     * Test updating a non-existent customer
     * Expected: Should return 404 Not Found
     */
    @Test
    void updateCustomer_WithNonExistentId_ShouldReturnNotFound() throws Exception {
        when(customerService.updateCustomer(eq(1L), any(Customer.class)))
                .thenThrow(new jakarta.persistence.EntityNotFoundException());

        mockMvc.perform(put("/customer/1")
                .with(csrf())
                .with(httpBasic("test", "test"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testCustomer)))
                .andExpect(status().isNotFound());

        verify(customerService, times(1)).updateCustomer(eq(1L), any(Customer.class));
    }

    /**
     * Test updating a non-existent customer
     * Expected: Should return 400 BadRequest
     */
    @Test
    void updateCustomer_WithNonExistentId_ShouldReturnBadRequest() throws Exception {

        when(customerService.updateCustomer(eq(1L), any(Customer.class)))
                .thenThrow(new IllegalArgumentException("Invalid data"));
        mockMvc.perform(put("/customer/1")
                .with(csrf())
                .with(httpBasic("test", "test"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testCustomer)))
                .andExpect(status().isBadRequest());

        verify(customerService, times(1)).updateCustomer(eq(1L), any(Customer.class));

    }

    /**
     * Test deleting an existing customer
     * Expected: Should return 200 OK
     */
    @Test
    void deleteCustomer_WithExistingId_ShouldReturnOk() throws Exception {
        doNothing().when(customerService).deleteCustomer(1L);

        mockMvc.perform(delete("/customer/1")
               .with(csrf())
               .with(httpBasic("test", "test")))
                
                .andExpect(status().isOk());

        verify(customerService, times(1)).deleteCustomer(1L);
    }

    /**
     * Test deleting a non-existent customer
     * Expected: Should return 404 Not Found
     */
    @Test
    void deleteCustomer_WithNonExistentId_ShouldReturnNotFound() throws Exception {
        doThrow(new jakarta.persistence.EntityNotFoundException())
                .when(customerService).deleteCustomer(1L);

        mockMvc.perform(delete("/customer/1")
               .with(csrf())
               .with(httpBasic("test", "test")))
                .andExpect(status().isNotFound());

        verify(customerService, times(1)).deleteCustomer(1L);
    }

    /**
     * Test deleting a non-existent customer
     * Expected: Should return 400 BadRequest
     */
    @Test
    void deleteCustomer_WithNonExistentId_ShouldReturnBadRequest() throws Exception {

        doThrow(new IllegalArgumentException("Invalid")).when(customerService).deleteCustomer(1L);

        mockMvc.perform(delete("/customer/1")
                .with(csrf())
                .with(httpBasic("test", "test")))
                .andExpect(status().isBadRequest());

        verify(customerService, times(1)).deleteCustomer(1L);

    }

    //Authentication

    /**
     * Test authentication failure with wrong credentials
     */
    @Test
    void whenInvalidCredentials_thenUnauthorized() throws Exception {
        mockMvc.perform(get("/customer")
                .with(csrf())
                .with(httpBasic("admin", "admin")))
                .andExpect(status().isUnauthorized());
    }

    
    /**
     * Test authentication failure with no credentials
     */
    @Test
    void whenNoCredentials_thenUnauthorized() throws Exception {
        mockMvc.perform(get("/customer")
               .with(csrf()))
               .andExpect(status().isUnauthorized());
    }

     /**
     * Test authentication with empty credentials
     */
    @Test
    void whenEmptyCredentials_thenUnauthorized() throws Exception {
        mockMvc.perform(get("/customer")
                .with(httpBasic("", "")))
                .andExpect(status().isUnauthorized());
    }


    
 

  

}

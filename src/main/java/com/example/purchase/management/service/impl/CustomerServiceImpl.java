package com.example.purchase.management.service.impl;

import com.example.purchase.management.entity.Customer;
import com.example.purchase.management.repository.CustomerRepository;
import com.example.purchase.management.service.CustomerService;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    @Override
    public Customer createCustomer(Customer customer) {
        // Validate customer data
        if (customer.getName() == null || customer.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Customer name is required");
        }
        return customerRepository.save(customer);
    }

    @Override
    public Customer updateCustomer(Long id, Customer customer) {
        try{
        // Validate customer data
        if (customer.getName() == null || customer.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Customer name is required");
        }
        return customerRepository.findById(id)
                .map(existingCustomer -> {
                    existingCustomer.setName(customer.getName());
                    existingCustomer.setPhone(customer.getPhone());
                    return customerRepository.save(existingCustomer);
                })
                .orElseThrow(() -> new EntityNotFoundException("Customer not found"));
    }catch (Exception e) {
        System.out.println("Error updating customer: " + e.getMessage());  // Debug log
        throw e;
    }}



    @Override
    public void deleteCustomer(Long id) {
        if (!customerRepository.existsById(id)) {
            throw new EntityNotFoundException("Customer not found");
        }
        customerRepository.deleteById(id);
    }

    @Override
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

}
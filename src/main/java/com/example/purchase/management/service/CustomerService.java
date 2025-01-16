package com.example.purchase.management.service;

import com.example.purchase.management.entity.Customer;
import java.util.List;

public interface CustomerService {

    Customer createCustomer(Customer customer);

    Customer updateCustomer(Long id, Customer customer);

    void deleteCustomer(Long id);

    List<Customer> getAllCustomers();

}

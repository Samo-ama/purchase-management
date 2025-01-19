package com.example.purchase.management.seeder;

import com.example.purchase.management.entity.*;
import com.example.purchase.management.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final PurchaseRepository purchaseRepository;
    private final RefundRepository refundRepository;

    @Override
    public void run(String... args) {
        // Seed Customers
        Customer customer1 = new Customer();
        customer1.setFirstName("Ahmad");
        customer1.setLastName("saad");
        customer1.setPhone("0912345678");

        Customer customer2 = new Customer();
        customer2.setFirstName("Hani");
        customer2.setLastName("saad");
        customer2.setPhone("0912345999");

        List<Customer> customers = customerRepository.saveAll(Arrays.asList(customer1, customer2));

        // Seed Products
        Product product1 = new Product();
        product1.setName("Laptop");
        product1.setPrice(1000.00);

        Product product2 = new Product();
        product2.setName("Phone");
        product2.setPrice(599.50);

        List<Product> products = productRepository.saveAll(Arrays.asList(product1, product2));

        // Seed Purchases
        Purchase purchase1 = new Purchase();
        purchase1.setCustomer(customers.get(0));
        purchase1.setProduct(products.get(0));
        purchase1.setAmount(10.00);
        purchase1.setDate(LocalDateTime.now().minusDays(1));

        Purchase purchase2 = new Purchase();
        purchase2.setCustomer(customers.get(1));
        purchase2.setProduct(products.get(1));
        purchase2.setAmount(15.00);
        purchase2.setDate(LocalDateTime.now().minusDays(1));

        List<Purchase> purchases = purchaseRepository.saveAll(Arrays.asList(purchase1, purchase2));

        // Seed Refunds
        Refund refund = new Refund();
        refund.setCustomer(customers.get(0));
        refund.setPurchase(purchases.get(0));
        refund.setAmount(7.00);
        refund.setDate(LocalDateTime.now().minusDays(1));

        refundRepository.save(refund);
    }
}
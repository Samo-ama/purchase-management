package com.example.purchase.management.repository;

import com.example.purchase.management.entity.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(
        connection = EmbeddedDatabaseConnection.H2
)
class ProductRepositoryTest {
    @Autowired
    private ProductRepository productRepository;
        
    private Product product1;
    private Product product2;
    private List<Product> all;

    @BeforeEach
    void setUp() {
        product1 = new Product();
        product1.setName("Dell Lap");
        product1.setPrice(1000.00);

        product2 = new Product();
        product1.setName("HP Lap");
        product1.setPrice(1200.00);

        all = productRepository.saveAll(List.of(product1, product2));
    }

    @Test
    public void findById_ProductExists_ShouldReturnProduct(){
        Optional<Product> firstProduct = productRepository.findById(product1.getId());

        assertTrue(firstProduct.isPresent());
        assertEquals(product1.getId() , firstProduct.get().getId());
    }

    @Test
    public void findById_ProductNotExist_ShouldReturnEmpty(){
        Optional<Product> notExistedProduct = productRepository.findById(999L);

        assertTrue(notExistedProduct.isEmpty());
    }

    @Test
    public void findAll_ShouldReturnAllProducts(){
        int expectedSize = all.size();

        List<Product> products = productRepository.findAll();
        int actualSize = all.size();

        assertNotNull(products);
        assertEquals(expectedSize, actualSize);
        assertEquals(all.get(0), products.get(0));
        assertEquals(all.get(expectedSize - 1), products.get(actualSize - 1));
    }

    @Test
    public void delete_ProductExists_ShouldRemoveProduct() {
        int productsSizeBefore = all.size();
        productRepository.delete(product2);

        Optional<Product> foundProduct = productRepository.findById(product2.getId());
        assertFalse(foundProduct.isPresent());
        assertEquals(productsSizeBefore - 1, productRepository.findAll().size());
    }

    @Test
    public void create_Product_ShouldSaveProduct(){
        // Act
        Product savedProduct = productRepository.save(product1);

        // Assert
        assertNotNull(savedProduct);
        assertEquals(product1.getName(), savedProduct.getName());
        assertEquals(product1.getPrice(), savedProduct.getPrice());

        Optional<Product> retrievedProduct = productRepository.findById(savedProduct.getId());

        assertTrue(retrievedProduct.isPresent());
        assertEquals(savedProduct.getId(), retrievedProduct.get().getId());
    }

    @Test
    public void update_Product_ShouldSaveUpdates(){
        // Arrange
        String newLapName = "Asus Lap ";
        Product existingProduct = productRepository.findById(product1.getId()).orElseThrow();

        // Act
        existingProduct.setName(newLapName);
        Product updatedProduct = productRepository.save(existingProduct);

        // Assert
        assertNotNull(updatedProduct);
        assertEquals(existingProduct.getId(), updatedProduct.getId());
        assertEquals(newLapName, updatedProduct.getName());

        Optional<Product> foundProduct = productRepository.findById(existingProduct.getId());
        assertTrue(foundProduct.isPresent());
        assertEquals(newLapName, foundProduct.get().getName());

    }

}

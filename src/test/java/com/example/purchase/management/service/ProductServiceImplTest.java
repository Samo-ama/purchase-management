package com.example.purchase.management.service;

import com.example.purchase.management.entity.Product;
import com.example.purchase.management.repository.ProductRepository;
import com.example.purchase.management.service.impl.ProductServiceImpl;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductServiceImplTest {

    @InjectMocks
    private ProductServiceImpl productService;
    @Mock
    private ProductRepository productRepository;
    private Product validProduct;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        validProduct = new Product();
        validProduct.setName("Laptop");
        validProduct.setPrice(1000.00);
    }

    @Test
    public void createProduct_WithValidData_ShouldSaveProduct(){
        //Arrange
        when(productRepository.save(validProduct)).thenReturn(validProduct);

        //Act
        Product actualProduct = productService.createProduct(validProduct);

        //Assert
        assertNotNull(actualProduct);
        assertEquals(validProduct.getName(), actualProduct.getName());
        assertEquals(validProduct.getPrice(), actualProduct.getPrice());
        verify(productRepository, times(1)).save(validProduct);
    }

    @Test
    public void createProduct_WithNullName_ShouldThrowIllegalArgException(){
        //Arrange
        validProduct.setName(null);
        when(productRepository.save(validProduct)).thenReturn(validProduct);

        //Act & Assert
        var exp = assertThrows(
                IllegalArgumentException.class,
                ()-> productService.createProduct(validProduct)
        );
        assertEquals("Product name is required", exp.getMessage());
        verify(productRepository, never()).save(validProduct);
    }

    @Test
    public void createProduct_WithEmptyName_ShouldThrowIllegalArgException(){
        //Arrange
        validProduct.setName(" ");
        when(productRepository.save(validProduct)).thenReturn(validProduct);

        //Act & Assert
        var exp = assertThrows(
                IllegalArgumentException.class,
                ()-> productService.createProduct(validProduct)
        );
        assertEquals("Product name is required", exp.getMessage());
        verify(productRepository, never()).save(validProduct);
    }

    @Test
    public void createProduct_WithNullPrice_ShouldThrowIllegalArgException(){
        //Arrange
        validProduct.setPrice(null);
        when(productRepository.save(validProduct)).thenReturn(validProduct);

        //Act & Assert
        var exp = assertThrows(
                IllegalArgumentException.class,
                ()-> productService.createProduct(validProduct)
        );
        assertEquals("Valid price is required", exp.getMessage());
        verify(productRepository, never()).save(validProduct);
    }

    @Test
    public void createProduct_WithNegativePrice_ShouldThrowIllegalArgException(){
        //Arrange
        validProduct.setPrice(-100.00);
        when(productRepository.save(validProduct)).thenReturn(validProduct);

        //Act & Assert
        var exp = assertThrows(
                IllegalArgumentException.class,
                ()-> productService.createProduct(validProduct)
        );
        assertEquals("Valid price is required", exp.getMessage());
        verify(productRepository, never()).save(validProduct);
    }

    @Test
    void updateProduct_WithValidData_ShouldUpdateProduct() {
        // Arrange
        when(productRepository.findById(1L)).thenReturn(Optional.of(validProduct));
        when(productRepository.save(any(Product.class))).thenReturn(validProduct);

        Product updateData = new Product();
        updateData.setName("HP lap");
        updateData.setPrice(200.00);

        // Act
        Product updatedProduct = productService.updateProduct(1L, updateData);

        // Assert
        assertNotNull(updatedProduct);
        assertEquals(updateData.getName(), updatedProduct.getName());
        assertEquals(updateData.getPrice(), updatedProduct.getPrice());
        verify(productRepository, times(1)).findById(1L);
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    public void updateProduct_WithNonExistedProduct_ShouldThrowNotFoundException() {
        // Arrange
        Long productId = 2L;
        Product updateData = new Product();
        updateData.setName("HP lap");
        updateData.setPrice(200.00);

        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        // Act & Assert
        var exp = assertThrows(
                EntityNotFoundException.class,
                () -> {
                    productService.updateProduct(productId, updateData);
                });
        assertEquals("Product not found", exp.getMessage());
    }

    @Test
    public void updateProduct_WithInvalidPrice_ShouldThrowIllegalArgException() {
        // Arrange
        Long productId = 1L;
        Product updatedProduct = new Product();
        updatedProduct.setName("Valid Name");
        updatedProduct.setPrice(-10.0);

        when(productRepository.findById(productId)).thenReturn(Optional.of(validProduct));

        // Act & Assert
        var exp = assertThrows(IllegalArgumentException.class, () -> {
            productService.updateProduct(productId, updatedProduct);
        });
        assertEquals("Valid price is required", exp.getMessage());
    }

    @Test
    public void updateProduct_WithInvalidName_ShouldThrowIllegalArgException() {
        // Arrange
        Long productId = 1L;
        Product updatedProduct = new Product();
        updatedProduct.setName(" ");
        updatedProduct.setPrice(10.0);

        when(productRepository.findById(productId)).thenReturn(Optional.of(validProduct));

        // Act & Assert
        var exp = assertThrows(IllegalArgumentException.class, () -> {
            productService.updateProduct(productId, updatedProduct);
        });
        assertEquals("Product name is required", exp.getMessage());
    }

    @Test
    public void deleteProduct_ProductExists_ShouldDeleteProduct() {
        // Arrange
        Long productId = 1L;

        when(productRepository.existsById(productId)).thenReturn(true);

        // Act
        productService.deleteProduct(productId);

        // Assert
        verify(productRepository, times(1)).deleteById(productId);
    }

    @Test
    public void getAllProducts_ShouldReturnListOfProducts() {
        // Arrange
        List<Product> products = new ArrayList<>();
        Product validProduct2 =new Product();
        validProduct2.setPrice(20.0);
        validProduct2.setName("Mobile");

        products.add(validProduct);
        products.add(validProduct2);

        when(productRepository.findAll()).thenReturn(products);

        // Act
        List<Product> returnedList = productService.getAllProducts();

        // Assert
        assertEquals(products.size(), returnedList.size());
        assertEquals(products, returnedList);
    }

    @Test
    public void getAllProducts_NoProducts_ShouldReturnEmptyList() {
        // Arrange
        List<Product> products = new ArrayList<>();

        when(productRepository.findAll()).thenReturn(products);

        // Act
        List<Product> returnedList = productService.getAllProducts();

        // Assert
        assertTrue(returnedList.isEmpty());
    }
}

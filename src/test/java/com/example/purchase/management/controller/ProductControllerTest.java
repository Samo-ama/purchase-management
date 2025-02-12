package com.example.purchase.management.controller;

import com.example.purchase.management.entity.Product;
import com.example.purchase.management.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)

public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    private Product product1;
    private Product product2;
    private List<Product> products;

    @BeforeEach
    void setUp() {

        product1 = new Product();
        product1.setId(1L);
        product1.setName("Dell Lap");
        product1.setPrice(1000.00);

        product2 = new Product();
        product2.setId(2L);
        product2.setName("HP Lap");
        product2.setPrice(1200.00);

        products = Arrays.asList(product1, product2);
    }

    @Test
    public void createProduct_WithUnauthenticatedUser_ShouldReturnForbidden() throws Exception {
        // Arrange
        when(productService.createProduct(any(Product.class))).thenReturn(product1);

        // Act
        ResultActions response = mockMvc.perform(
                post("/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product1))
        );

        // Assert
        response.andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser
    public void createProduct_WithValidProduct_ShouldReturnCreatedProduct() throws Exception {
        // Arrange
        when(productService.createProduct(any(Product.class))).thenReturn(product1);

        // Act
        ResultActions response = mockMvc.perform(
                post("/product")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product1))
        );

        // Assert
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(product1.getName()))
                .andExpect(jsonPath("$.price").value(product1.getPrice()));
    }

    @Test
    @WithMockUser
    public void createProduct_WithInvalidProduct_ShouldReturnBadRequest() throws Exception {
        // Arrange
        when(productService.createProduct(any(Product.class)))
                .thenThrow(IllegalArgumentException.class);

        // Act
        ResultActions response = mockMvc.perform(
                post("/product")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product1))
        );

        // Assert
        response.andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    public void getAllProducts_ShouldReturnAllProducts() throws Exception {
        // Arrange
        when(productService.getAllProducts()).thenReturn(products);

        // Act
        ResultActions response = mockMvc.perform(get("/product")
        );

//        String responseBody = response.andReturn().getResponse().getContentAsString();
//        System.out.println("response : " + responseBody);

        // Assert
        response.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name").value(product1.getName()))
                .andExpect(jsonPath("$[1].name").value(product2.getName()))
                .andExpect(jsonPath("$[0].price").value(product1.getPrice()))
                .andExpect(jsonPath("$[1].price").value(product2.getPrice()));
    }

    @Test
    @WithMockUser
    public void updateProduct_WithValidProduct_ShouldUpdateProduct() throws Exception {
        // Arrange
        Long productId = 1L;
        when(productService.updateProduct(eq(productId), any(Product.class))).thenReturn(product1);
//        when(productService.updateProduct(productId, product1)).thenReturn(product1);

        // Act
        ResultActions response = mockMvc.perform(
                        put("/product/{id}", productId)
                                .with(SecurityMockMvcRequestPostProcessors.csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(product1))
        );

//        String responseBody = response.andReturn().getResponse().getContentAsString();
//        System.out.println("response : " + responseBody);

        // Assert
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(product1.getName()))
                .andExpect(jsonPath("$.price").value(product1.getPrice()));
    }

    @Test
    public void updateProduct_WithInValidProduct_ShouldReturnBadRequest() throws Exception {
        // Arrange
        Long productId = 1L;
        when(productService.updateProduct(anyLong(), any(Product.class)))
                .thenThrow(IllegalArgumentException.class);

        // Act
        ResultActions response = mockMvc.perform(
                put("/product/{id}", productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product1))
        );
    }

    @Test
    @WithMockUser
    public void updateProduct_WithNonExistentProduct_ShouldReturnNotFound() throws Exception {
        // Arrange
        Long productId = 99L;
        when(productService.updateProduct(anyLong(), any(Product.class)))
                .thenThrow(EntityNotFoundException.class);

        // Act
        ResultActions response = mockMvc.perform(
                        put("/product/{id}", productId)
                                .with(SecurityMockMvcRequestPostProcessors.csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(product1))
        );

        // Assert
        response.andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    public void deleteProduct_WithExistentProduct_ShouldDeleteProduct() throws Exception {
        // Arrange
        Long productId = 1L;
        doNothing().when(productService).deleteProduct(productId);

        // Act
        ResultActions response = mockMvc.perform(
                delete("/product/{id}", productId)
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
        );

        //Assert
        response.andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    public void deleteProduct_WithNonExistentProduct_ShouldReturnNotFound() throws Exception {
        // Arrange
        Long productId = 1L;
        doThrow(EntityNotFoundException.class).when(productService).deleteProduct(productId);

        // Act
        ResultActions response = mockMvc.perform(
                delete("/product/{id}", productId)
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
        );

        //Assert
        response.andExpect(status().isNotFound());
    }
}
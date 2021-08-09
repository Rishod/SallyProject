package com.sally.shop.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sally.shop.dao.ProductDAO;
import com.sally.shop.dao.entity.ProductEntity;
import com.sally.shop.models.CreateProductRequest;
import com.sally.shop.models.Product;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.UUID;

@ExtendWith({MockitoExtension.class})
class ProductServiceTest {

    @InjectMocks
    private ProductService productService;
    @Mock
    private ProductDAO productDAO;

    @Test
    void testSaveProduct() {
        final UUID shopId = UUID.randomUUID();
        String name = "name";
        String description = "desc";
        BigDecimal price = new BigDecimal("12.34");

        final CreateProductRequest createProductRequest = CreateProductRequest.builder()
                .name(name)
                .description(description)
                .price(price)
                .build();

        final ProductEntity productEntity = new ProductEntity();
        productEntity.setId(UUID.randomUUID());

        when(productDAO.saveProduct(shopId, name, description, price))
                .thenReturn(productEntity);


        final Product result = productService.saveProduct(shopId, createProductRequest);

        assertThat(result.getId()).isEqualTo(productEntity.getId());
    }

    @Test
    void deleteProduct() {
        final UUID shopId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();

        productService.deleteProduct(shopId, productId);

        verify(productDAO).delete(shopId, productId);
    }
}
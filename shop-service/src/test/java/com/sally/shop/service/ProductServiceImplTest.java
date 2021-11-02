package com.sally.shop.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sally.api.Product;
import com.sally.api.requests.CreateProductRequest;
import com.sally.auth.ShopDetails;
import com.sally.shop.dao.ProductDAO;
import com.sally.shop.dao.entity.ProductEntity;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@ExtendWith({MockitoExtension.class})
class ProductServiceImplTest {

    @InjectMocks
    private ProductServiceImpl productService;
    @Mock
    private ProductDAO productDAO;
    @Mock
    private CommandGateway commandGateway;

    @Test
    void testSaveProduct() {
        final UUID shopId = UUID.randomUUID();
        final String shopName = "shopName";
        String name = "name";
        String description = "desc";
        BigDecimal price = new BigDecimal("12.34");

        final ShopDetails shopDetails = new ShopDetails();
        shopDetails.setId(shopId);
        shopDetails.setName(shopName);

        final CreateProductRequest createProductRequest = CreateProductRequest.builder()
                .name(name)
                .description(description)
                .price(price)
                .build();

        final ProductEntity productEntity = new ProductEntity();
        productEntity.setId(UUID.randomUUID());

//        when(productDAO.saveProduct(shopId, name, description, price))
//                .thenReturn(productEntity);


//        final Product result = productService.saveProduct(shopDetails, createProductRequest);

//        assertThat(result.getId()).isEqualTo(productEntity.getId());
    }

    @Test
    void deleteProduct() {
        final UUID shopId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();

        final ProductEntity productEntity = new ProductEntity();
        productEntity.setId(productId);
        productEntity.setShopId(shopId);

        when(productDAO.getProductByIdAndShopId(productId, shopId)).thenReturn(Optional.of(productEntity));
        productService.deleteProduct(shopId, productId);

        verify(productDAO).delete(shopId, productId);
    }
}
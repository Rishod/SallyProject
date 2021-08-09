package com.sally.shop.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.sally.exceptions.NotFoundException;
import com.sally.shop.DaoTest;
import com.sally.shop.dao.entity.ProductEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

class ProductDAOImplTest extends DaoTest {

    @Autowired
    private ProductDAOImpl productDAO;

    @Test
    void testCreateProduct() {
        final UUID shopId = UUID.randomUUID();

        final ProductEntity result = productDAO.saveProduct(shopId, "name", "description", new BigDecimal("19.55"));

        final ProductEntity productEntity = testEntityManager.find(ProductEntity.class, result.getId());
        assertThat(productEntity).isEqualTo(result);
    }

    @Test
    void testUpdateProduct() {
        // SETUP
        final UUID shopId = UUID.randomUUID();

        final ProductEntity productEntity = prepareProduct(shopId);

        // ACT
        final ProductEntity result = productDAO.updateProduct(productEntity.getId(), shopId,
                "updatedName", "updatedDesc", new BigDecimal("99.99"));

        // VERIFY
        final ProductEntity updatedProduct = testEntityManager.find(ProductEntity.class, result.getId());

        assertThat(result).isEqualTo(updatedProduct);
        assertThat(productEntity).isEqualTo(updatedProduct);
    }

    @Test
    void testFindProductByIdAndShopId() {
        // SETUP
        final UUID shopId = UUID.randomUUID();

        final ProductEntity productEntity = prepareProduct(shopId);

        // ACT
        final Optional<ProductEntity> result = productDAO.findProductByIdAndShop(productEntity.getId(), shopId);

        // VERIFY
        assertThat(result).isNotEmpty();
        assertThat(result).contains(productEntity);
    }

    @Test
    void testGetAllProducts() {
        final List<ProductEntity> products = Arrays.asList(
                prepareProduct(UUID.randomUUID()),
                prepareProduct(UUID.randomUUID()),
                prepareProduct(UUID.randomUUID())
        );

        final List<ProductEntity> result = productDAO.getAllProducts();

        assertThat(result).size().isEqualTo(3);
        assertThat(result).containsAll(products);
    }

    @Test
    void testDeleteProduct() {
        // SETUP
        final UUID shopId = UUID.randomUUID();
        final ProductEntity productEntity = prepareProduct(shopId);

        // ACT
        productDAO.delete(shopId, productEntity.getId());

        // VERIFY
        final ProductEntity deletedEntities = testEntityManager.find(ProductEntity.class, productEntity.getId());
        assertThat(deletedEntities).isNull();
    }

    @Test
    void testDeleteNotExistedProduct() {
        assertThrows(NotFoundException.class, () -> productDAO.delete(UUID.randomUUID(), UUID.randomUUID()));
    }

    private ProductEntity prepareProduct(UUID shopId) {
        final ProductEntity productEntity = ProductEntity.builder()
                .name("name")
                .description("desc")
                .price(new BigDecimal("20.55"))
                .shopId(shopId)
                .build();

        testEntityManager.persist(productEntity);

        return productEntity;
    }
}
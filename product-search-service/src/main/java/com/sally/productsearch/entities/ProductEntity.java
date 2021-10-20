package com.sally.productsearch.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "products")
public class ProductEntity {
    @Id
    private UUID id;

    private String title;

    private String description;

    private String shopName;

    private UUID shopId;

    private BigDecimal price;
}

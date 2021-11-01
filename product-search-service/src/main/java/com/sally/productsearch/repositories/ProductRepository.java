package com.sally.productsearch.repositories;

import com.sally.productsearch.entities.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProductRepository extends ElasticsearchRepository<ProductEntity, UUID> {
    Page<ProductEntity> findByTitle(String title, Pageable pageable);

    @Query("{\n" +
            "    \"multi_match\" : {\n" +
            "      \"query\":    \"?0\",\n" +
            "      \"fields\": [ \"title\", \"description\", \"shopName\" ]\n" +
            "    }\n" +
            "  }")
    Page<ProductEntity> defaultSearch(final String searchText, Pageable pageable);

    List<ProductEntity> findByIdIn(final List<UUID> ids);

}

package com.sally.productsearch.repositories;

import com.sally.productsearch.entities.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends ElasticsearchRepository<ProductEntity, String> {
    Page<ProductEntity> findByTitle(String title, Pageable pageable);
}

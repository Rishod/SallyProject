package com.sally.productsearch.repositories;

import com.sally.productsearch.entities.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

@RepositoryRestResource(path = "products")
public interface ProductRepository extends ElasticsearchRepository<ProductEntity, String> {

    @RestResource(path = "find")
    Page<ProductEntity> findByTitle(String title, Pageable pageable);
}

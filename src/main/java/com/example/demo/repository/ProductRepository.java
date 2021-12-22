package com.example.demo.repository;

import com.example.demo.domain.MeasureUnit;
import com.example.demo.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

@RepositoryRestResource(path = "products")
public interface ProductRepository extends PagingAndSortingRepository<Product, Long> {
    @RestResource(path = "names")
    Page<Product> findByName(String q, Pageable pageable);

    @RestResource(path = "unit")
    Page<Product> findByUnit(MeasureUnit q, Pageable pageable);

    @RestResource(path = "description")
    Page<Product> findByDescriptionContaining(String q, Pageable pageable);
}

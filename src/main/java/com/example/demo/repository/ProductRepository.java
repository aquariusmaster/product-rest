package com.example.demo.repository;

import com.example.demo.domain.MeasureUnit;
import com.example.demo.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.Description;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

@RepositoryRestResource(path = "products")
public interface ProductRepository extends PagingAndSortingRepository<Product, Long> {
    @RestResource(path = "name", description = @Description("search by name"))
    Page<Product> findByNameContaining(String q, Pageable pageable);

    @RestResource(path = "unit", description = @Description("search by unit"))
    Page<Product> findByUnit(MeasureUnit q, Pageable pageable);

    @RestResource(path = "description", description = @Description("search by description"))
    Page<Product> findByDescriptionContaining(String q, Pageable pageable);
}

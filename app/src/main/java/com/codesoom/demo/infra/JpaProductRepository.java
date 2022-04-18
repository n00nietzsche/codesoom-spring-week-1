package com.codesoom.demo.infra;

import com.codesoom.demo.domain.Product;
import com.codesoom.demo.domain.ProductRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

@Primary
public interface JpaProductRepository extends ProductRepository, CrudRepository<Product, Long> {
    @Override
    List<Product> findAll();
    @Override
    Optional<Product> findById(long id);
    @Override
    Product save(Product product);
    @Override
    void delete(Product product);
}

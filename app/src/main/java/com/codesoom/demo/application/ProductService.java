package com.codesoom.demo.application;

import com.codesoom.demo.domain.Product;
import com.codesoom.demo.domain.ProductRepository;
import com.codesoom.demo.dto.ProductDto;
import com.codesoom.demo.exceptions.ProductNotFoundException;
import com.github.dozermapper.core.Mapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

// TODO: getProducts -> 목록
// TODO: getProduct -> 상세 정보
// TODO: createProduct -> 상품 추가
// TODO: updateProduct -> 상품 수정
// TODO: deleteProduct -> 상품 삭제

/**
 * Service for products.
 *
 * @author Jake Seo (n00nietzsche@gmail.com)
 */
@Service
@Transactional
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final Mapper mapper;

    /**
     * Returns all products in this application.
     *
     * @return all products.
     */
    public List<Product> getProducts() {
        // TODO: 실제로 구현할 것
        return productRepository.findAll();
    }

    /**
     * Retrieves a product by its id.
     *
     * @param id an identifier of the product.
     * @return a product with given ID.
     * @throws ProductNotFoundException in case, the given ID doesn't exist in repository or is already deleted.
     */
    public Product getProduct(Long id) {
        // TODO: 실제로 구현할 것
        return findProduct(id);
    }

    public Product createProduct(ProductDto productDto) {
        Product product = mapper.map(productDto, Product.class);

        return productRepository.save(product);
    }

    public Product updateProduct(long id, ProductDto productDto) {
        Product product = findProduct(id);
        // 순수한 엔티티는 DTO 를 모르는 것이 좋다.
        product.changeWith(mapper.map(productDto, Product.class));

        return product;
    }

    public Product deleteProduct(long id) {
        // TODO: 실제로 구현할 것
        Product product = findProduct(id);
        productRepository.delete(product);
        return product;
    }

    public Product findProduct(long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }
}

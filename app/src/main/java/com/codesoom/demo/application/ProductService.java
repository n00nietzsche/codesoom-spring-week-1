package com.codesoom.demo.application;

import com.codesoom.demo.domain.Product;
import com.codesoom.demo.domain.ProductRepository;
import com.codesoom.demo.exceptions.ProductNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

// TODO: getProducts -> 목록
// TODO: getProduct -> 상세 정보
// TODO: createProduct -> 상품 추가
// TODO: updateProduct -> 상품 수정
// TODO: deleteProduct -> 상품 삭제

@Service
@Transactional
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getProducts() {
        // TODO: 실제로 구현할 것
        return productRepository.findAll();
    }

    public Product getProduct(Long id) {
        // TODO: 실제로 구현할 것
        return findProduct(id);
    }

    public Product createProduct(Product product) {
        // TODO: 실제로 구현할 것
        return productRepository.save(product);
    }

    public Product updateProduct(long id, Product source) {
        // TODO: 실제로 구현할 것
        Product product = findProduct(id);
        product.change(source);

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

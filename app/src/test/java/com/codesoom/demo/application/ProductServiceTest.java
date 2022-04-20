package com.codesoom.demo.application;

import com.codesoom.demo.domain.Product;
import com.codesoom.demo.domain.ProductRepository;
import com.codesoom.demo.dto.ProductDto;
import com.codesoom.demo.exceptions.ProductNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

// TODO: getProducts -> 목록
// TODO: getProduct -> 상세 정보
// TODO: createProduct -> 상품 추가
// TODO: updateProduct -> 상품 수정
// TODO: deleteProduct -> 상품 삭제

class ProductServiceTest {

    private final ProductRepository productRepository = mock(ProductRepository.class);
    private final ProductService productService = new ProductService(productRepository);
    private Product product;


    @BeforeEach
    void setUp() {
        product = Product.builder()
                .id(1L)
                .name("쥐돌이")
                .maker("냥이월드")
                .price(3000)
                .build();

        given(productRepository.findAll()).willReturn(List.of(product));
        given(productRepository.findById(1L)).willReturn(Optional.of(product));
        given(productRepository.findById(1000L)).willThrow(ProductNotFoundException.class);
        given(productRepository.save(any(Product.class))).will(invocation -> {
            Product product = invocation.getArgument(0);
            product.setId(2L);
            return product;
        });
    }

    @Test
    void getProducts() {
        List<Product> products = productService.getProducts();
        assertThat(products).isNotEmpty();
        assertThat(products.get(0).getName()).isEqualTo("쥐돌이");
        verify(productRepository).findAll();
    }

    @Test
    void getProductsNoProducts() {
        given(productRepository.findAll()).willReturn(List.of());
        assertThat(productService.getProducts()).isEmpty();
        verify(productRepository).findAll();
    }

    @Test
    void getProduct() {
        Product product = productService.getProduct(1L);
        assertThat(product).isNotNull();
        assertThat(product.getName()).isEqualTo("쥐돌이");
        verify(productRepository).findById(1L);
    }

    @Test
    void getProductNotFound() {
        assertThatThrownBy(() -> productService.getProduct(1000L))
                .isInstanceOf(ProductNotFoundException.class);
    }

    @Test
    void createProduct() {
        ProductDto productDto = ProductDto.builder()
                .name("쥐돌이55")
                .maker("냥이월드")
                .price(3000)
                .build();

        Product product = productService.createProduct(productDto);
        assertThat(product).isNotNull();
        assertThat(product.getName()).isEqualTo("쥐돌이55");
        assertThat(product.getId()).isEqualTo(2L);
        verify(productRepository).save(any(Product.class));
    }

    @Test
    void updateProduct() {
        ProductDto productDto = ProductDto.builder()
                .name("쥐순이")
                .maker("냥이월드")
                .price(3000)
                .build();

        Product product = productService.updateProduct(1L, productDto);

        assertThat(product).isNotNull();
        assertThat(product.getName()).isEqualTo("쥐순이");
        assertThat(product.getId()).isEqualTo(1L);

        verify(productRepository).findById(1L);
    }

    @Test
    void updateProductNotFound() {
        ProductDto productDto = ProductDto.builder()
                .name("쥐순이")
                .maker("냥이월드")
                .price(3000)
                .build();

        assertThatThrownBy(() -> productService.updateProduct(1000L, productDto))
                .isInstanceOf(ProductNotFoundException.class);
    }

    @Test
    void deleteProduct() {
        Product product = productService.deleteProduct(1L);

        assertThat(product).isNotNull();
        assertThat(product.getName()).isEqualTo("쥐돌이");
        assertThat(product.getId()).isEqualTo(1L);

        verify(productRepository).findById(1L);
        verify(productRepository).delete(product);
    }

    @Test
    void deleteProductNotFound() {
        assertThatThrownBy(() -> productService.deleteProduct(1000L))
                .isInstanceOf(ProductNotFoundException.class);
    }
}
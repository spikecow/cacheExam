package com.amore.example.cache.controller;

import com.amore.example.cache.domain.Product;
import com.amore.example.cache.repository.ProductRepository;
import com.amore.example.cache.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RequiredArgsConstructor
@RestController
public class ProductController {

    private final ProductService productService;
    private final ProductRepository productRepository;

    @GetMapping("/product/{no}")
    public Optional<Product> getProduct(@PathVariable Long no) {
        return productService.findProduct(no);
    }

    @PostMapping("/product")
    public Product create(Product param){

        Product product = productRepository.save(param);
        productService.productCacheRefresh(); // 원본 데이터 추가로 인한 cache refresh
        return product;
    }

    @DeleteMapping("/product/{no}")
    public void delete(@PathVariable Long no){
        productRepository.deleteById(no);
        productService.productCacheRefresh(); // 원본 데이터 삭제로 인한 cache refresh
    }

    @PutMapping("/product/{no}")
    public Product update(Product param){
        Product product = productRepository.save(param);
        productService.productCacheRefresh(); // 원본 데이터 변경으로 인한 cache refresh
        return product;
    }
}

package com.amore.example.cache.service;

import com.amore.example.cache.domain.Product;

import java.util.Optional;

public interface ProductService {
    Optional<Product> findProduct(Long no);
    void setProductList();
    void productCacheRefresh();

}

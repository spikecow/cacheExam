package com.amore.example.cache.service;

import com.amore.example.cache.domain.Category;

import java.util.Map;
import java.util.Optional;

public interface CategoryService {
    Map<Long,Category> findCategoryList();
    Optional<Category> findCategory(Long no);
    void setCategoryList();
    void categoryCacheRefresh();

}

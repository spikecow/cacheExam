package com.amore.example.cache.controller;

import com.amore.example.cache.domain.Category;
import com.amore.example.cache.repository.CategoryRepository;
import com.amore.example.cache.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
public class CategoryController {

    private final CategoryService categoryService;
    private final CategoryRepository categoryRepository;

    @GetMapping("/category/list")
    public Iterable<Category> list() {
        return new ArrayList<>(categoryService.findCategoryList().values());
    }

    @GetMapping("/category/{no}")
    public Optional<Category> getCategory(@PathVariable Long no) {
        return categoryService.findCategory(no);
    }

    @PostMapping("/category")
    public Category create(Category param){

        Category category = categoryRepository.save(param);
        categoryService.categoryCacheRefresh(); // 원본 데이터 추가로 인한 cache refresh
        return category;
    }

    @DeleteMapping("/category/{no}")
    public void delete(@PathVariable Long no){
        categoryRepository.deleteById(no);
        categoryService.categoryCacheRefresh(); // 원본 데이터 삭제로 인한 cache refresh
    }

    @PutMapping("/category/{no}")
    public Category update(Category param){
        Category category = categoryRepository.save(param);
        categoryService.categoryCacheRefresh(); // 원본 데이터 변경으로 인한 cache refresh
        return category;
    }

}

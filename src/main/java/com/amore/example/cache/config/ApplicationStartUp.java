package com.amore.example.cache.config;

import com.amore.example.cache.service.CategoryService;
import com.amore.example.cache.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ApplicationStartUp implements ApplicationListener<ApplicationReadyEvent> {

    private final CategoryService categoryService;
    private final ProductService productService;

    @Override
    public void onApplicationEvent(final ApplicationReadyEvent event) {
        categoryService.setCategoryList();
        productService.setProductList();
    }
}

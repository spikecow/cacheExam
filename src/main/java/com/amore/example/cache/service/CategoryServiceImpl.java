package com.amore.example.cache.service;

import com.amore.example.cache.domain.Category;
import com.amore.example.cache.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@RequiredArgsConstructor
@Service
public class CategoryServiceImpl implements CategoryService{
    private final int CACHE_SIZE = 15;
    private final CategoryRepository categoryRepository;
    //private final Map<Long, Category> categoryList = new ConcurrentHashMap<>();

    private long categoryListLoadTime;
    private final long categroyListCacheDuration = 100 * 1000L;

    /*
     * 최근 동안 가장 사용되지 않았던 카테고리 정보 부터 eviction 합니다.
     * 이유는 사용자가 특정 카테고리의 상품의 검색률이 가장 낮다는 것이기 때문입니다.
     */
    LinkedHashMap<Long, Category> categoryList = new LinkedHashMap<>(CACHE_SIZE, 0.75f, true) {
        protected boolean removeEldestEntry(Map.Entry<Long, Category> eldest) {
            return size() > CACHE_SIZE;
        }
    };

    @Override
    public Map<Long,Category> findCategoryList() {
        setCategoryList();
        return categoryList;
    }

    @Override
    public Optional<Category> findCategory(Long no) {
        setCategoryList();

        Optional<Category> category;
        category = Optional.ofNullable(categoryList.get(no));
        // 캐시 내에 존재 하지 않을 경우 DB 에서 가져와 캐시에 적재 한다.
        if (category.isEmpty()) {
            category = categoryRepository.findById(no);
        }
        categoryList.put(category.get().getCategoryNo(), category.get());
        return category;
    }

    @Override
    public void setCategoryList() {

        long now = System.currentTimeMillis();

        // application 을 거치지 않고 db client 로 직접 데이터를 crud 할 경우를 위해 cache refresh time 둔다.
        if (categoryList.isEmpty() || now - categoryListLoadTime > categroyListCacheDuration) {

            LinkedHashMap<Long, Category> map = new LinkedHashMap<>();
            List<Category> list = categoryRepository.findAll();

            for(Category c : list){
                map.put(c.getCategoryNo(), c);
            }

            categoryList.clear();
            categoryList.putAll(map);
            categoryListLoadTime = now;
        }

    }

    @Override
    public void categoryCacheRefresh() {
        categoryList.clear();
        setCategoryList();
    }
}

package com.amore.example.cache.service;

import com.amore.example.cache.domain.Product;
import com.amore.example.cache.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ProductServiceImpl implements ProductService{
    private final int CACHE_SIZE = 1010;
    private final ProductRepository productRepository;
    //private final Map<Long, Product> productList = new ConcurrentHashMap<>();

    private long productListLoadTime;
    private final long productListCacheDuration = 100 * 1000L;

    LinkedHashMap<Long, Product> productList = new LinkedHashMap<>(CACHE_SIZE, 0.75f, true) {
        protected boolean removeEldestEntry(Map.Entry<Long, Product> eldest) {
            return size() > CACHE_SIZE;
        }
    };

    @Override
    public Optional<Product> findProduct(Long no) {
        setProductList();

        Optional<Product> product;
        product = Optional.ofNullable(productList.get(no));
        // 캐시 내에 존재 하지 않을 경우 DB 에서 가져와 캐시에 적재 한다.
        if (product.isEmpty()) {
            product = productRepository.findById(no);
        }
        productList.put(product.get().getProductNo(), product.get());
        return product;
    }

    @Override
    public void setProductList() {

        long now = System.currentTimeMillis();

        // application 을 거치지 않고 db client 로 직접 데이터를 crud 할 경우를 위해 cache refresh time 둔다.
        if (productList.isEmpty() || now - productListLoadTime > productListCacheDuration) {

            LinkedHashMap<Long, Product> map = new LinkedHashMap<>();
            List<Product> list = productRepository.findAll();

            for(Product p : list){
                map.put(p.getProductNo(), p);
            }

            productList.clear();
            productList.putAll(map);
            productListLoadTime = now;
        }
    }

    @Override
    public void productCacheRefresh() {
        productList.clear();
        setProductList();
    }
}

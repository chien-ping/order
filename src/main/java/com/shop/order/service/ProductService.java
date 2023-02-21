package com.shop.order.service;

import com.shop.order.convetor.ProductConvertor;
import com.shop.order.dto.ProductDto;
import com.shop.order.exception.ValidationException;
import com.shop.order.repository.ProductRepository;
import com.shop.order.vo.PlaceOrderItemVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * 商品操作
 */
@Slf4j
@Service
public class ProductService {
    @Autowired
    ProductRepository productRepository;

    private final ConcurrentHashMap<Long, AtomicInteger> prodStockCache = new ConcurrentHashMap<>();

    /**
     * 載入商品和庫存到Cache
     */
    @PostConstruct
    public void loadProdStocksInMemory(){
        productRepository.findAll().parallelStream()
                .forEach(p -> prodStockCache.put(p.getProdId(), new AtomicInteger(p.getStock())));
    }

    /**
     * 扣除庫存
     * @param items 訂單明細
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void reduceInventory(List<PlaceOrderItemVo> items) {
        reduceStockCache(items);
        try{
            items.stream().forEach(i -> productRepository.reduceStock(i.getProdId(), i.getNum()));
        }catch(Exception e){
            log.error("", e);
            rollbackStockCache(items);
            throw e;
        }
    }

    /**
     * 檢查庫存Cache
     * @param items 訂單明細
     */
    private void reduceStockCache(List<PlaceOrderItemVo> items){
        boolean outOfStock = false;
        List<PlaceOrderItemVo> reduced = new ArrayList<>();
        for(PlaceOrderItemVo item : items){
            AtomicInteger stock = prodStockCache.get(item.getProdId());
            int nowStock = stock.get();
            if(nowStock - item.getNum()>= 0){
                boolean success = stock.compareAndSet(nowStock, nowStock-item.getNum());
                if(success) {
                    log.warn("prodId: {}, now stock:{}", item.getProdId(), nowStock - item.getNum());
                    reduced.add(item);
                }else{
                    outOfStock = true;
                    break;
                }
            }else{
                outOfStock = true;
                break;
            }
        }
        if(outOfStock){
            if(reduced.size()>0)
                rollbackStockCache(reduced);
            throw new ValidationException("validation.product.out_of_stock", "product out of stock");
        }

    }

    /**
     * 回滾庫存
     * @param items
     */
    public void rollbackInventory(List<PlaceOrderItemVo> items){
        rollbackStockCache(items);
    }

    /**
     * 回滾庫存Cache
     * @param items 訂單資料
     */
    private void rollbackStockCache(List<PlaceOrderItemVo> items){
        items.stream().forEach(i -> prodStockCache.get(i.getProdId()).addAndGet(i.getNum()));
    }

    /**
     * 根據商品編號查詢商品
     * @param prodIds 商品編號
     * @return 商品資料
     */
    public List<ProductDto> queryById(List<Long> prodIds) {
        return productRepository.findAllById(prodIds).stream()
                .map(p -> ProductConvertor.INSTANCE.toDto(p)).collect(Collectors.toList());
    }
}

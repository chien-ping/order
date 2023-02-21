package com.shop.order.dto;

import lombok.Data;

/**
 * 商品資料
 */
@Data
public class ProductDto {

    /**
     * 商品編號
     */
    private Long prodId;

    /**
     * 商品名稱
     */
    private String prodName;

    /**
     * 商品單價
     */
    private Long amount;
}

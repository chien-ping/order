package com.shop.order.dto;

import lombok.Data;

/**
 * 訂單明細資料
 */
@Data
public class OrderItemDto {

    /**
     * 序號
     */
    private Long seq;

    /**
     * 商品名稱
     */
    private String prodName;

    /**
     * 商品單價
     */
    private Long amount;
}

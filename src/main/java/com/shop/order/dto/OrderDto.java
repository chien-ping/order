package com.shop.order.dto;

import lombok.Data;

import java.util.List;

/**
 * 訂單資料
 */
@Data
public class OrderDto {

    /**
     * 訂單編號
     */
    private Long orderId;

    /**
     * 購買人
     */
    private MemberDto member;

    /**
     * 購買日期
     */
    private String orderDate;

    /**
     * 購買明細
     */
    private List<OrderItemDto> items;
}

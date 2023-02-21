package com.shop.order.vo;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import lombok.Data;

/**
 * 訂單查詢條件
 */
@Data
public class OrderQueryVo {

    /**
     * 訂單編號
     */
    private Long orderId;

    /**
     * 商品名稱
     */
    private String prodName;

    /**
     * 購買日期-開始
     */
    private String orderDateBegin;

    /**
     * 購買日期-結束
     */
    private String orderDateEnd;

    /**
     * 頁數
     */
    @JsonSetter(nulls = Nulls.SKIP)
    private Integer page = 0;

    /**
     * 每頁筆數
     */
    @JsonSetter(nulls = Nulls.SKIP)
    private Integer size = 20;
}

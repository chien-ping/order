package com.shop.order.vo;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * 建立訂單明細資料
 */
@Data
public class PlaceOrderItemVo {

    /**
     * 商品編號
     */
    @NotNull(message = "validation.order.item.prod_id.is_necessary")
    private Long prodId;


    /**
     * 購買數量
     */
    @NotNull(message = "validation.order.item.num.is_necessary")
    @Min(1)
    @Max(999)
    private Integer num;

}

package com.shop.order.vo;

import com.shop.order.dto.MemberDto;
import com.shop.order.dto.OrderItemDto;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * 建立訂單資料
 */
@Data
public class PlaceOrderVo {

    /**
     * 購買人
     */
    @NotNull(message = "validation.order.memId.is_necessary")
    private Long memId;

    /**
     * 購買明細
     */
    @NotNull(message = "validation.order.item.is_necessary")
    @Size(min = 1, max = 999, message = "validation.order.item.not_over_999")
    private List<PlaceOrderItemVo> items;
}

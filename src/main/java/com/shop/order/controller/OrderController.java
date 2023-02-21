package com.shop.order.controller;


import com.shop.order.dto.OrderDto;
import com.shop.order.vo.OrderQueryVo;
import com.shop.order.service.OrderService;
import com.shop.order.vo.PagingVo;
import com.shop.order.vo.PlaceOrderVo;
import com.shop.order.vo.ResponseVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RequestMapping("/v1/order")
@RestController
public class OrderController {

    @Autowired
    OrderService orderService;

    /**
     * 查詢訂單
     * @param condition 查詢條件
     * @return 訂單資料
     */
    @GetMapping(value = "")
    public PagingVo<OrderDto> getOrders(@Valid @ModelAttribute OrderQueryVo condition) {
        PagingVo<OrderDto> orders = orderService.query(condition);
        return orders;
    }

    /**
     * 新增訂單
     * @param order 訂單資料
     * @return 執行結果
     */
    @PostMapping(value="")
    public ResponseVo placeOrder(@Valid @RequestBody PlaceOrderVo order) {
        log.warn("Place order : {}", order.toString());
        orderService.placeOrder(order);
        return new ResponseVo("execution.success");
    }
}

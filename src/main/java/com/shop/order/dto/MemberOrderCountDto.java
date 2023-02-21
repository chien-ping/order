package com.shop.order.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 查詢會員訂單數
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class MemberOrderCountDto {

    /**
     * 會員編號
     */
    private Long memId;

    /**
     * 會員帳號
     */
    private String account;

    /**
     * 會員姓名
     */
    private String name;

    /**
     * 訂單數
     */
    private Long orderCount;

}

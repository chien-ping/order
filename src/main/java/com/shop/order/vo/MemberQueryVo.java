package com.shop.order.vo;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import com.shop.order.enums.MemberLevel;
import lombok.Data;

/**
 * 會員查詢條件
 */
@Data
public class MemberQueryVo {

    /**
     * 會員編號
     */
    private Long memId;

    /**
     * 會員帳號
     */
    private String account;

    /**
     * 是否啟用
     */
    @JsonSetter(nulls = Nulls.SKIP)
    private boolean inUse = true;

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

    private MemberLevel memberLevel;
}

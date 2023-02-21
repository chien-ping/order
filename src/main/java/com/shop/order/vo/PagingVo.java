package com.shop.order.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 分頁查詢結果
 */
@Data
public class PagingVo<T> {

    /**
     * 總筆數
     */
    private long total = 0;

    /**
     * 分頁數
     */
    private int page = 0;

    /**
     * 回傳資料筆數
     */
    private int size = 0;

    /**
     * 列表資料
     */
    private List<T> rows = new ArrayList<>();
}

package com.shop.order.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

/**
 * 執行結果
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseVo {

    /**
     * 執行結果
     */
    private String message;

    /**
     * 錯誤訊息
     */
    private List<String> errors;

    public ResponseVo() {
    }

    public ResponseVo(String message) {
        this.message = message;
    }
}

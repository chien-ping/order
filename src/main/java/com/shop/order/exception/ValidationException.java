package com.shop.order.exception;

import lombok.Getter;

/**
 * 資料檢查例外
 */
public class ValidationException extends RuntimeException {
    /**
     * 錯誤碼
     */
    @Getter
    private String code;

    /**
     * 錯誤訊息
     */
    @Getter
    private String message;

    public ValidationException(String code) {
        super(code);
        this.code = code;
    }

    public ValidationException(String code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }
}

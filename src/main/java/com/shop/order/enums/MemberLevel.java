package com.shop.order.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Slf4j
public enum MemberLevel {

    /**
     * 一般會員
     */
    NORMAL("1", "一般會員"),

    /**
     * VIP會員
     */
    VIP("2", "VIP會員"),

    /**
     * 高級VIP會員
     */
    SUPER_VIP("3", "高級VIP會員");

    private String code;
    private String desc;

    MemberLevel(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    @JsonValue
    public String getCode() {
        return code;
    }

    @JsonCreator
    public static MemberLevel of(String code) {
        if (null == code) {
            return null;
        }

        for (MemberLevel item : MemberLevel.values()) {
            if (code.equals(item.getCode())) {
                return item;
            }
        }

        return null;
    }
}

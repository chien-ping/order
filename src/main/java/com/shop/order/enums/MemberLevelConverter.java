package com.shop.order.enums;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * 參考來源:
 * https://github.com/test-project-demos/spring-boot-get-post-enum-param/tree/master
 */
@Component
public class MemberLevelConverter implements Converter<String, MemberLevel> {
    @Override
    public MemberLevel convert(String value) {
        return MemberLevel.of(value);
    }
}

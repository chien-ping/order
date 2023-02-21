package com.shop.order.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * 會員資料
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MemberDto {

    /**
     * 會員編號
     */
    private Long memId;

    /**
     * 會員帳號
     */
    @NotEmpty(message = "validation.member.account.not_empty")
    @Pattern(regexp = "^[a-zA-Z0-9]{1,50}$", message = "validation.member.account.pattern")
    private String account;

    /**
     * 會員姓名
     */
    @NotEmpty(message = "validation.member.name.not_empty")
    @Size(max = 50, message = "validation.name.length_limit_50")
    private String name;

}

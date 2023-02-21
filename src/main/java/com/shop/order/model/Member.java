package com.shop.order.model;

import lombok.Data;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

/**
 * 會員
 */
@Data
@Entity
@Table(name = "member")
public class Member {

    /**
     * Id 自增主鍵
     */
    @Id
    @Column(name = "mem_id")
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long memId;

    /**
     * 帳號
     */
    @Column(name = "mem_account")
    private String account;

    /**
     * 姓名
     */
    @Column(name = "mem_name")
    private String name;

    /**
     * 啟用狀態
     */
    @Column(name = "in_use")
    private Boolean inUse;

    /**
     * 建立時間
     */
    @Column(name = "created_date")
    private Date createdDate;

    /**
     * 最後更新時間
     */
    @UpdateTimestamp
    @Column(name = "modified_date")
    private Date modifiedDate;
}

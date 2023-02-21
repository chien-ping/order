package com.shop.order.model;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;

/**
 * 訂單明細
 */
@Data
@Entity
@Table(name = "order_item")
public class OrderItem {

    /**
     * Id 自增主鍵
     */
    @Id
    @Column(name = "item_id")
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long itemId;

    /**
     * 序號
     */
    @Column(name = "seq",nullable = false)
    private Integer seq;


    /**
     * 訂單主檔
     */
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="order_id", referencedColumnName = "order_id")
    private Order order;

    /**
     * 商品編號
     */
    @Column(name = "prod_id")
    private Long prodId;

    /**
     * 結帳商品名稱
     */
    @Column(name = "prod_name")
    private String prodName;

    /**
     * 結帳價格
     */
    @Column(name = "amount")
    private Long amount;
}

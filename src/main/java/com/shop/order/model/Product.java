package com.shop.order.model;

import lombok.Data;

import javax.persistence.*;

/**
 * 商品
 */
@Data
@Entity
@Table(name = "product")
public class Product {

    /**
     * Id 自增主鍵
     */
    @Id
    @Column(name = "prod_id")
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long prodId;

    /**
     * 商品名稱
     */
    @Column(name = "prod_name")
    private String prodName;

    /**
     * 價格
     */
    @Column(name = "amount")
    private Long amount;

    /**
     * 庫存
     */
    @Column(name = "stock")
    private Integer stock;
}

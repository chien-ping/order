package com.shop.order.model;

import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * 訂單
 */
@Data
@Entity
@Table(name = "order_main")
public class Order {

    /**
     * Id 自增主鍵
     */
    @Id
    @Column(name = "order_id")
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long orderId;

    /**
     * 會員編號
     */
    @Column(name = "mem_id")
    private Long memId;


    /**
     * 購買會員
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="mem_id", referencedColumnName = "mem_id", insertable = false, updatable = false)
    private Member member;

    /**
     * 訂單金額
     */
    @Column(name = "total_amt")
    private Long totalAmt;

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

    /**
     * 訂單明細
     */
    @OrderBy("order_id,seq asc")
    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<OrderItem> items;
}

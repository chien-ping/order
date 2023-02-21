package com.shop.order.repository;

import com.shop.order.dto.MemberOrderCountDto;
import com.shop.order.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("select new com.shop.order.dto.MemberOrderCountDto(o.member.memId, o.member.account, o.member.name, count(*))" +
            "  from Order o group by o.member having count(*) > :orderCount order by count(*) desc")
    List<MemberOrderCountDto> findMembersWithOverNOrder(Long orderCount);

    Page<Order> findByOrderId(Long orderId, Pageable pageable);

    Page<Order> findByItems_ProdNameLike(String trim, Pageable pageable);

    Page<Order> findByCreatedDateBetween(Date startDate, Date endDate, Pageable pageable);
}

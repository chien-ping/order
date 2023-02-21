package com.shop.order.service;

import com.shop.order.convetor.OrderConvertor;
import com.shop.order.dto.*;
import com.shop.order.exception.ValidationException;
import com.shop.order.model.*;
import com.shop.order.repository.OrderRepository;
import com.shop.order.vo.MemberQueryVo;
import com.shop.order.vo.OrderQueryVo;
import com.shop.order.vo.PagingVo;
import com.shop.order.vo.PlaceOrderVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 訂單操作
 */
@Slf4j
@Service
public class OrderService {

    @Autowired
    MemberService memberService;

    @Autowired
    OrderRepository orderRepository;
    @Autowired
    ProductService productService;

    /**
     * 查詢訂單
     * @param condition 查詢條件
     * @return 訂單資料
     */
    public PagingVo<OrderDto> query(OrderQueryVo condition) {
        Page<Order> orders  = null;
        Pageable pageable = PageRequest.of(condition.getPage(), condition.getSize(), Sort.by("createdDate").descending());
        if(condition.getOrderId() != null){
            orders = orderRepository.findByOrderId(condition.getOrderId(), pageable);
        }else if(condition.getProdName() != null){
            orders = orderRepository.findByItems_ProdNameLike("%" + condition.getProdName() + "%",pageable);
        }else if(condition.getOrderDateBegin() != null &&
                condition.getOrderDateEnd() != null){
            Date startDate = null;
            Date endDate = null;
            try{
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                startDate = sdf.parse(condition.getOrderDateBegin());
                endDate = new Date(sdf.parse(condition.getOrderDateEnd()).getTime() + 86400000);
            }catch(Exception e){
                throw new ValidationException("Validation.order.date_format.wrong");
            }
            orders = orderRepository.findByCreatedDateBetween(startDate, endDate, pageable);
        }else {
            orders = orderRepository.findAll(pageable);
        }
        PagingVo<OrderDto> dto = new PagingVo<>();
        if(orders != null){
            dto.setTotal(orders.getTotalElements());
            dto.setPage(orders.getNumber());
            dto.setSize(orders.getNumberOfElements());
            dto.setRows(orders.get().map(o-> OrderConvertor.INSTANCE.toDto(o)).collect(Collectors.toList()));
        }

        return dto;
    }

    /**
     * 建立訂單
     * @param order 訂單資料
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void placeOrder(PlaceOrderVo order) {
        // 檢查會員
        MemberQueryVo condition = new MemberQueryVo();
        condition.setMemId(order.getMemId());
        condition.setInUse(true);
        if(memberService.query(condition).getSize() == 0)
            throw new ValidationException("validation.member.account.not_exist",
                        "Create order fail : account does not exists : " + order.getMemId());
        // 檢查商品
        List<Long> prodIds = order.getItems().stream().map(i -> i.getProdId()).collect(Collectors.toList());
        Map<Long,ProductDto> prods = productService.queryById(prodIds).stream()
                .collect(Collectors.toMap(ProductDto::getProdId, Function.identity()));
        if(prodIds.size() != prods.size())
            throw new ValidationException("validation.product.prodId.not_exist",
                    "Create order fail : some products does not exist : " +
                            prodIds.stream().map(i -> i.toString()).collect(Collectors.joining(",")));

        productService.reduceInventory(order.getItems());
        try{
            Order entity = OrderConvertor.INSTANCE.toModel(order);
            Long totalAmt = entity.getItems().stream()
                    .peek(i -> i.setOrder(entity))
                    .peek(i -> i.setProdName(prods.get(i.getProdId()).getProdName()))
                    .peek(i -> i.setAmount(prods.get(i.getProdId()).getAmount()))
                    .mapToLong(i -> prods.get(i.getProdId()).getAmount()).sum();
            entity.setTotalAmt(totalAmt);
            entity.setCreatedDate(new Date());
            log.error("isEmpty order : {}", entity.getItems().get(0).getOrder());
            orderRepository.save(entity);
        }catch(Exception e){
            log.error("", e);
            productService.rollbackInventory(order.getItems());
            throw e;
        }
    }

    /**
     * 查詢訂單數大於N的會員
     * @param orderCount 訂單數
     * @return 會員資料
     */
    public List<MemberOrderCountDto> queryOrderCountOver(Long orderCount) {
        return orderRepository.findMembersWithOverNOrder(orderCount);
    }
}

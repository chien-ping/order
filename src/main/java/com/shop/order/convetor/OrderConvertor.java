package com.shop.order.convetor;

import com.shop.order.dto.OrderDto;
import com.shop.order.vo.OrderQueryVo;
import com.shop.order.model.Order;
import com.shop.order.model.OrderItem;
import com.shop.order.vo.PlaceOrderItemVo;
import com.shop.order.vo.PlaceOrderVo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 商品物件轉換
 */
@Mapper
public interface OrderConvertor {

    OrderConvertor INSTANCE = Mappers.getMapper( OrderConvertor.class );

    @Mapping(source="createdDate", target = "orderDate",  dateFormat = "yyyy-MM-dd HH:mm:ss")
    OrderDto toDto(Order model);

    @Mapping(source="items", target="items",qualifiedByName="mapOrderItem")
    @Mapping(source="memId", target="member.memId")
    Order toModel(PlaceOrderVo vo);

    @Named("mapOrderItem")
    default List<OrderItem> mapOrderItem(List<PlaceOrderItemVo> items){
        AtomicInteger seq = new AtomicInteger(0);
        return items.stream().map(i -> splitItem(i))
                .flatMap(i -> i.stream()).peek(i -> i.setSeq(seq.incrementAndGet()))
                .collect(Collectors.toList());
    }

    default List<OrderItem> splitItem(PlaceOrderItemVo item){
        return IntStream.rangeClosed(1, item.getNum())
                .mapToObj(i -> toModel(item))
                .collect(Collectors.toList());
    }

    OrderItem toModel(PlaceOrderItemVo vo);

}

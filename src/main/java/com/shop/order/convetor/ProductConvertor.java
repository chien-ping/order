package com.shop.order.convetor;

import com.shop.order.dto.ProductDto;
import com.shop.order.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 商品物件轉換
 */
@Mapper
public interface ProductConvertor {

    ProductConvertor INSTANCE = Mappers.getMapper( ProductConvertor.class );

    ProductDto toDto(Product model);

}

package com.shop.order.convetor;

import com.shop.order.dto.MemberDto;
import com.shop.order.vo.MemberQueryVo;
import com.shop.order.model.Member;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 會員物件轉換
 */
@Mapper
public interface MemberConvertor {

    MemberConvertor INSTANCE = Mappers.getMapper( MemberConvertor.class );

    MemberDto toDto(Member model);

    Member toModel(MemberDto dto);

    Member toModel(MemberQueryVo vo);

}

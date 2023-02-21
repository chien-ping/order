package com.shop.order.service;

import com.shop.order.convetor.MemberConvertor;
import com.shop.order.dto.MemberDto;
import com.shop.order.vo.MemberQueryVo;
import com.shop.order.exception.ValidationException;
import com.shop.order.model.Member;
import com.shop.order.repository.MemberRepository;
import com.shop.order.vo.PagingVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.stream.Collectors;

/**
 * 會員操作
 */
@Slf4j
@Service
public class MemberService {

    @Autowired
    MemberRepository memberRepository;

    /**
     * 查詢會員
     * @param condition 查詢條件
     * @return 會員資料
     */
    public PagingVo<MemberDto> query(MemberQueryVo condition) {
        Member member = MemberConvertor.INSTANCE.toModel(condition);
        Pageable pageable = PageRequest.of(condition.getPage(), condition.getSize(), Sort.by("memId").ascending());
        Page<Member> members = memberRepository.findAll(Example.of(member), pageable);
        PagingVo<MemberDto> dto = new PagingVo<>();
        dto.setTotal(members.getTotalElements());
        dto.setPage(members.getNumber());
        dto.setSize(members.getNumberOfElements());
        dto.setRows(members.get().map(m-> MemberConvertor.INSTANCE.toDto(m)).collect(Collectors.toList()));
        return dto;
    }

    /**
     * 新增會員
     * @param member 會員資料
     */
    public synchronized void add(MemberDto member) {
        Member memInUse = new Member();
        memInUse.setAccount(member.getAccount());
        memInUse.setInUse(true);
        memberRepository.findOne(Example.of(memInUse)).ifPresent(
                m -> { throw new ValidationException("validation.member.account.already_exist",
                        "Add member fail : account already exists : " + member.getAccount());});
        Member entity = MemberConvertor.INSTANCE.toModel(member);
        entity.setInUse(true);
        entity.setCreatedDate(new Date());
        memberRepository.save(entity);
    }


    /**
     * 更新會員
     * @param member 會員資料
     */
    public void update(MemberDto member) {
        Member memInUse = new Member();
        memInUse.setMemId(member.getMemId());
        memInUse.setAccount(member.getAccount());
        memInUse.setInUse(true);
        Member entity = memberRepository.findOne(Example.of(memInUse)).
                orElseThrow(() -> new ValidationException("validation.member.account.not_exist",
                        "Update member fail : account does not exists : " + member.getAccount()));
        entity.setName(member.getName());
        memberRepository.save(entity);
    }

    /**
     * 停用會員
     * @param memId 會員編號
     */
    public void disable(Long memId) {
        Member memInUse = new Member();
        memInUse.setMemId(memId);
        memInUse.setInUse(true);
        Member entity = memberRepository.findOne(Example.of(memInUse)).
                orElseThrow(() -> new ValidationException("validation.member.account.not_exist",
                        "Disable member fail : account does not exists : " + memId));
        entity.setInUse(false);
        memberRepository.save(entity);
    }
}

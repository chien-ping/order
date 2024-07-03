package com.shop.order.service;

import com.shop.order.dto.MemberDto;
import com.shop.order.exception.ValidationException;
import com.shop.order.model.Member;
import com.shop.order.repository.MemberRepository;
import com.shop.order.vo.MemberQueryVo;
import com.shop.order.vo.PagingVo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    private MemberRepository mockMemberRepository;

    private MemberService memberServiceUnderTest;

    @BeforeEach
    void setUp() {
        memberServiceUnderTest = new MemberService();
        memberServiceUnderTest.memberRepository = mockMemberRepository;
    }

    @Test
    void testQuery() {
        // Setup
        final MemberQueryVo condition = new MemberQueryVo();
        condition.setMemId(0L);
        condition.setAccount("account");
        condition.setInUse(false);
        condition.setPage(0);
        condition.setSize(1);

        MemberDto memberDto = new MemberDto();
        memberDto.setMemId(0L);
        memberDto.setAccount("account");
        memberDto.setName("name");

        final PagingVo<MemberDto> expectedResult = new PagingVo<>();
        expectedResult.setTotal(1L);
        expectedResult.setPage(0);
        expectedResult.setSize(1);
        expectedResult.setRows(Collections.singletonList(memberDto));

        // Configure MemberRepository.findAll(...).
        final Member member = new Member();
        member.setMemId(0L);
        member.setAccount("account");
        member.setName("name");
        member.setInUse(false);
        member.setCreatedDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        final Page<Member> members = new PageImpl<>(Arrays.asList(member));
        when(mockMemberRepository.findAll(any(Example.class), any(Pageable.class))).thenReturn(members);

        // Run the test
        final PagingVo<MemberDto> result = memberServiceUnderTest.query(condition);

        // Verify the results
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void testQuery_MemberRepositoryReturnsNoItems() {
        // Setup
        final MemberQueryVo condition = new MemberQueryVo();
        condition.setMemId(0L);
        condition.setAccount("account");
        condition.setInUse(false);
        condition.setPage(0);
        condition.setSize(1);

        final PagingVo<MemberDto> expectedResult = new PagingVo<>();
        expectedResult.setTotal(0L);
        expectedResult.setPage(0);
        expectedResult.setSize(0);
        expectedResult.setRows(Arrays.asList());

        when(mockMemberRepository.findAll(any(Example.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(Collections.emptyList()));

        // Run the test
        final PagingVo<MemberDto> result = memberServiceUnderTest.query(condition);

        // Verify the results
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void testAdd_ThrowsValidationException() {
        // Setup
        final MemberDto member = new MemberDto();
        member.setMemId(0L);
        member.setAccount("account");
        member.setName("name");

        // Configure MemberRepository.findOne(...).
        final Member member2 = new Member();
        member2.setMemId(0L);
        member2.setAccount("account");
        member2.setName("name");
        member2.setInUse(false);
        member2.setCreatedDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        final Optional<Member> member1 = Optional.of(member2);
        when(mockMemberRepository.findOne(any(Example.class))).thenReturn(member1);

        // Run the test
        assertThatThrownBy(() -> memberServiceUnderTest.add(member)).isInstanceOf(ValidationException.class);
    }

    @Test
    void testAdd_MemberRepositoryFindOneReturnsAbsent() {
        // Setup
        final MemberDto member = new MemberDto();
        member.setMemId(0L);
        member.setAccount("account");
        member.setName("name");

        when(mockMemberRepository.findOne(any(Example.class))).thenReturn(Optional.empty());

        // Run the test
        memberServiceUnderTest.add(member);

        // Confirm MemberRepository.save(...).
        verify(mockMemberRepository).save(any(Member.class));
    }

    @Test
    void testUpdate() {
        // Setup
        final MemberDto member = new MemberDto();
        member.setMemId(0L);
        member.setAccount("account");
        member.setName("name");

        // Configure MemberRepository.findOne(...).
        final Member member2 = new Member();
        member2.setMemId(0L);
        member2.setAccount("account");
        member2.setName("name");
        member2.setInUse(false);
        member2.setCreatedDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        final Optional<Member> member1 = Optional.of(member2);
        when(mockMemberRepository.findOne(any(Example.class))).thenReturn(member1);

        // Run the test
        memberServiceUnderTest.update(member);

        // Verify the results
        // Confirm MemberRepository.save(...).
        final Member entity = new Member();
        entity.setMemId(0L);
        entity.setAccount("account");
        entity.setName("name");
        entity.setInUse(false);
        entity.setCreatedDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        verify(mockMemberRepository).save(entity);
    }

    @Test
    void testUpdate_MemberRepositoryFindOneReturnsAbsent() {
        // Setup
        final MemberDto member = new MemberDto();
        member.setMemId(0L);
        member.setAccount("account");
        member.setName("name");

        when(mockMemberRepository.findOne(any(Example.class))).thenReturn(Optional.empty());

        // Run the test
        assertThatThrownBy(() -> memberServiceUnderTest.update(member)).isInstanceOf(ValidationException.class);
    }

    @Test
    void testDisable() {
        // Setup
        // Configure MemberRepository.findOne(...).
        final Member member1 = new Member();
        member1.setMemId(0L);
        member1.setAccount("account");
        member1.setName("name");
        member1.setInUse(false);
        member1.setCreatedDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        final Optional<Member> member = Optional.of(member1);
        when(mockMemberRepository.findOne(any(Example.class))).thenReturn(member);

        // Run the test
        memberServiceUnderTest.disable(0L);

        // Verify the results
        // Confirm MemberRepository.save(...).
        final Member entity = new Member();
        entity.setMemId(0L);
        entity.setAccount("account");
        entity.setName("name");
        entity.setInUse(false);
        entity.setCreatedDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        verify(mockMemberRepository).save(entity);
    }

    @Test
    void testDisable_MemberRepositoryFindOneReturnsAbsent() {
        // Setup
        when(mockMemberRepository.findOne(any(Example.class))).thenReturn(Optional.empty());

        // Run the test
        assertThatThrownBy(() -> memberServiceUnderTest.disable(0L)).isInstanceOf(ValidationException.class);
    }
}

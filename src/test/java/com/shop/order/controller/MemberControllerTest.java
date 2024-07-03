package com.shop.order.controller;

import com.shop.order.dto.MemberDto;
import com.shop.order.service.MemberService;
import com.shop.order.vo.ResponseVo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class MemberControllerTest {
    @Autowired
    private TestRestTemplate restTemplate;
    @MockBean
    private MemberService memberService;
    @Autowired
    private MemberController memberController;

    @Test
    @DisplayName("Should return an error when adding a member with invalid data")
    void addMemberWithInvalidDataThenReturnError() {
        MemberDto invalidMember = new MemberDto();
        invalidMember.setAccount("invalid_account");
        invalidMember.setName("Invalid Name");

        ResponseEntity<ResponseVo> response =
                restTemplate.postForEntity("/v1/member", invalidMember, ResponseVo.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getErrors());
        assertFalse(response.getBody().getErrors().isEmpty());
        verify(memberService, times(0)).add(any(MemberDto.class));
    }

    @Test
    @DisplayName("Should add a new member successfully")
    void addMemberSuccessfully() {
        MemberDto memberDto = new MemberDto();
        memberDto.setMemId(1L);
        memberDto.setAccount("testAccount");
        memberDto.setName("testName");

        doNothing().when(memberService).add(memberDto);

        ResponseEntity<ResponseVo> response =
                restTemplate.postForEntity("/v1/member", memberDto, ResponseVo.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("execution.success", response.getBody().getMessage());

        verify(memberService, times(1)).add(memberDto);
    }
}
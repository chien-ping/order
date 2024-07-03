package com.shop.order.controller;

import com.shop.order.dto.MemberDto;
import com.shop.order.dto.MemberOrderCountDto;
import com.shop.order.service.MemberService;
import com.shop.order.service.OrderService;
import com.shop.order.vo.MemberQueryVo;
import com.shop.order.vo.PagingVo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(MemberController.class)
class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MemberService mockMemberService;
    @MockBean
    private OrderService mockOrderService;

    @Test
    void testGetMembers() throws Exception {
        // Setup
        // Configure MemberService.query(...).
        final PagingVo<MemberDto> memberDtoPagingVo = new PagingVo<>();
        memberDtoPagingVo.setTotal(0L);
        memberDtoPagingVo.setPage(0);
        memberDtoPagingVo.setSize(0);
        memberDtoPagingVo.setRows(Arrays.asList());
        final MemberQueryVo condition = new MemberQueryVo();
        condition.setMemId(0L);
        condition.setAccount("account");
        condition.setInUse(false);
        condition.setPage(0);
        condition.setSize(0);
        when(mockMemberService.query(condition)).thenReturn(memberDtoPagingVo);

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(get("/v1/member")
                        .content("{\"memId\":1,\"memberLevel\":\"2\"}").contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("");
    }

}

package com.shop.order.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shop.order.dto.OrderDto;
import com.shop.order.service.OrderService;
import com.shop.order.vo.OrderQueryVo;
import com.shop.order.vo.PagingVo;
import com.shop.order.vo.PlaceOrderVo;
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
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ExtendWith(SpringExtension.class)
@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService mockOrderService;

    @Test
    void testGetOrders() throws Exception {
        // Setup
        // Configure OrderService.query(...).
        final PagingVo<OrderDto> orderDtoPagingVo = new PagingVo<>();
        orderDtoPagingVo.setTotal(0L);
        orderDtoPagingVo.setPage(0);
        orderDtoPagingVo.setSize(0);
        orderDtoPagingVo.setRows(Arrays.asList());
        final OrderQueryVo condition = new OrderQueryVo();
        condition.setOrderId(0L);
        condition.setProdName("prodName");
        condition.setOrderDateBegin("orderDateBegin");
        condition.setOrderDateEnd("orderDateEnd");
        condition.setPage(0);
        when(mockOrderService.query(condition)).thenReturn(orderDtoPagingVo);

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(get("/v1/order")
                        .content("content").contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("");
    }

    @Test
    void testPlaceOrder() throws Exception {
        // 获取 JSON 文件
        File file = ResourceUtils.getFile("classpath:test-data.json");

        // 创建 ObjectMapper 实例
        ObjectMapper objectMapper = new ObjectMapper();

        // 将 JSON 文件转换为 Java 对象列表
        List<PlaceOrderVo> testData = objectMapper.readValue(file,
                objectMapper.getTypeFactory().constructCollectionType(List.class, PlaceOrderVo.class));

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(post("/v1/order")
                        .content(objectMapper.writeValueAsString(testData.get(0))).contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("{\"message\":\"execution.success\"}");

    }
}

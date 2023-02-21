package com.shop.order.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shop.order.repository.OrderRepository;
import com.shop.order.vo.PlaceOrderVo;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@SpringBootTest
@ActiveProfiles("test")
public class OrderServiceTest {

    @Autowired
    OrderService orderService;

    @Autowired
    OrderRepository orderRepository;

    /**
     * 測試同時結帳
     */
    @Test
    public void placeOrder_compete() throws JsonProcessingException {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        String req1 = "{\n" +
                "    \"memId\": 1,\n" +
                "    \"items\":[\n" +
                "        {\n" +
                "            \"prodId\": 2,\n" +
                "            \"num\":2\n" +
                "        }\n" +
                "    ]\n" +
                "}";
        String req2 = "{\n" +
                "    \"memId\": 2,\n" +
                "    \"items\":[\n" +
                "        {\n" +
                "            \"prodId\": 2,\n" +
                "            \"num\":1\n" +
                "        }\n" +
                "    ]\n" +
                "}";


        ObjectMapper om = new ObjectMapper();
        PlaceOrderVo order1 = om.readValue(req1, PlaceOrderVo.class);
        PlaceOrderVo order2 = om.readValue(req2, PlaceOrderVo.class);
        PlaceOrderVo order3 = om.readValue(req2, PlaceOrderVo.class);
        PlaceOrderVo order4 = om.readValue(req1, PlaceOrderVo.class);

        List<PlaceOrderVo> orders = new ArrayList<>();
        orders.add(order1);
        orders.add(order2);
        orders.add(order3);
        orders.add(order4);

        int successCnt = orders.parallelStream().mapToInt(o -> {
            try{
                orderService.placeOrder(o);
                return 1;
            }catch(Exception e){
                log.error("",e.getMessage());
            }
           return 0;
        }).sum();

        assertEquals(1, successCnt);
        assertEquals(1, orderRepository.findAll().size());


    }
}

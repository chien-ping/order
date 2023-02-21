package com.shop.order.controller;

import com.shop.order.dto.MemberDto;
import com.shop.order.vo.MemberQueryVo;
import com.shop.order.exception.ValidationException;
import com.shop.order.dto.MemberOrderCountDto;
import com.shop.order.service.MemberService;
import com.shop.order.service.OrderService;
import com.shop.order.vo.PagingVo;
import com.shop.order.vo.ResponseVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Api(value = "desc of class")
@Slf4j
@RequestMapping("/v1/member")
@RestController
public class MemberController {

    @Autowired
    MemberService memberService;
    @Autowired
    OrderService orderService;


    /**
     * 查詢會員
     * @param condition 查詢條件
     * @return 會員資訊
     */
    @ApiOperation(value = "desc of method", notes = "")
    @GetMapping(value = "")
    public PagingVo<MemberDto> getMembers(@Valid @ModelAttribute  MemberQueryVo condition) {

        PagingVo<MemberDto> members = memberService.query(condition);
        return members;
    }

    /**
     * 查詢訂單數大於N的會員
     * @param  orderCount 訂單數
     * @return 會員資料
     */
    @GetMapping(value = "/orderCount/{orderCount}")
    public List<MemberOrderCountDto> getMembersByOrder(@PathVariable("orderCount") Long orderCount) {
        return orderService.queryOrderCountOver(orderCount);
    }

    /**
     * 查詢會員
     * @param memId 會員編號
     * @return 會員資訊
     */
    @GetMapping(value = "/{memId}")
    public MemberDto getMember(@PathVariable("memId")Long memId) {
        MemberQueryVo condition = new MemberQueryVo();
        condition.setMemId(memId);
        PagingVo<MemberDto> members = getMembers(condition);
        if(members.getSize() > 0)
            return members.getRows().get(0);
        else
            throw new ValidationException("validation.member.account.not_exist",
                    "account does not exists : " + memId);
    }

    /**
     * 新增會員
     * @param member 會員資料
     * @return 執行結果
     */
    @PostMapping(value="")
    public ResponseVo addMember(@Valid @RequestBody MemberDto member) {
        log.warn("Add member : {}", member.toString());
        memberService.add(member);
        return new ResponseVo("execution.success");
    }

    /**
     * 更新會員
     * @param member 會員資料
     * @return 執行結果
     */
    @PatchMapping(value="/{memId}")
    public ResponseVo updateMember(@Valid @RequestBody MemberDto member) {
        log.warn("Update member : {}", member.toString());
        memberService.update(member);
        return new ResponseVo("execution.success");
    }

    /**
     * 刪除會員
     * @param  memId 會員帳號
     * @return 執行結果
     */
    @DeleteMapping(value="/{memId}")
    public ResponseVo deleteMember(@NotEmpty @PathVariable("memId")Long memId) {
        log.warn("Delete member : {}", memId);
        memberService.disable(memId);
        return new ResponseVo("execution.success");
    }
}

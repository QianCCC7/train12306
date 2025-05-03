package com.xiaoqian.member.controller.admin;


import com.xiaoqian.common.domain.ResponseResult;
import com.xiaoqian.common.query.PageVo;
import com.xiaoqian.member.domain.query.MemberTicketQueryDTO;
import com.xiaoqian.member.domain.vo.MemberTicketVo;
import com.xiaoqian.member.service.IMemberTicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 乘客购买的车票记录 前端控制器
 * </p>
 *
 * @author xiaoqian
 * @since 2025-05-03
 */
@RestController
@RequestMapping("/admin/member-ticket")
@RequiredArgsConstructor
public class MemberTicketController {
    private final IMemberTicketService passengerTicketService;

    @GetMapping("/listMemberTicketPage")
    public ResponseResult<PageVo<MemberTicketVo>> listMemberTicketPage(MemberTicketQueryDTO query) {
        return passengerTicketService.listMemberTicketPage(query);
    }
}

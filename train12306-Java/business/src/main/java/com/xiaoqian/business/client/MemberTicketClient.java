package com.xiaoqian.business.client;

import com.xiaoqian.common.domain.ResponseResult;
import com.xiaoqian.common.domain.dto.MemberTicketDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "member", path = "/member/admin/member-ticket")
public interface MemberTicketClient {
    @PostMapping("/saveMemberTicket")
    ResponseResult<Void> saveMemberTicket(@RequestBody MemberTicketDTO memberTicketDTO);
}

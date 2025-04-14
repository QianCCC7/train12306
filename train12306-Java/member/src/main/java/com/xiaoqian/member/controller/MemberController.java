package com.xiaoqian.member.controller;


import com.xiaoqian.common.domain.ResponseResult;
import com.xiaoqian.member.domain.dto.MemberRegisterDTO;
import com.xiaoqian.member.service.IMemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 会员 前端控制器
 * </p>
 *
 * @author xiaoqian
 * @since 2025-04-14
 */
@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {
    private final IMemberService memberService;

    @PostMapping("/register")
    public ResponseResult<Long> register(@Valid @RequestBody MemberRegisterDTO memberRegisterDTO) {
        return memberService.register(memberRegisterDTO);
    }

}

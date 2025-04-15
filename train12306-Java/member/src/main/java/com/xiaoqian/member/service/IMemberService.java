package com.xiaoqian.member.service;

import com.xiaoqian.common.domain.ResponseResult;
import com.xiaoqian.member.domain.dto.MemberRegisterDTO;
import com.xiaoqian.member.domain.pojo.Member;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 会员 服务类
 * </p>
 *
 * @author xiaoqian
 * @since 2025-04-14
 */
public interface IMemberService extends IService<Member> {
    ResponseResult<Long> register(MemberRegisterDTO memberRegisterDTO);

    ResponseResult<String> sendCode(MemberRegisterDTO memberRegisterDTO);
}

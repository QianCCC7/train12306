package com.xiaoqian.member.service.impl;

import com.xiaoqian.common.domain.ResponseResult;
import com.xiaoqian.member.domain.dto.MemberRegisterDTO;
import com.xiaoqian.member.domain.pojo.Member;
import com.xiaoqian.member.mapper.MemberMapper;
import com.xiaoqian.member.service.IMemberService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 会员 服务实现类
 * </p>
 *
 * @author xiaoqian
 * @since 2025-04-14
 */
@Service
@RequiredArgsConstructor
public class MemberServiceImpl extends ServiceImpl<MemberMapper, Member> implements IMemberService {
    public ResponseResult<Long> register(MemberRegisterDTO memberRegisterDTO) {
        Member oldMember = lambdaQuery()
                .eq(Member::getMobile, memberRegisterDTO.getMobile())
                .one();
        if (oldMember != null) {
            throw new RuntimeException("手机号已存在");
        }
        Member member = new Member(System.currentTimeMillis(), memberRegisterDTO.getMobile());
        save(member);

        return ResponseResult.okResult(member.getId());
    }
}

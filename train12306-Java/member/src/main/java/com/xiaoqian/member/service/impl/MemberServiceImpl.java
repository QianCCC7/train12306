package com.xiaoqian.member.service.impl;

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
    public Long register(MemberRegisterDTO memberRegisterDTO) {
        Member member = new Member(System.currentTimeMillis(), memberRegisterDTO.getMobile());
        save(member);
        return member.getId();
    }
}

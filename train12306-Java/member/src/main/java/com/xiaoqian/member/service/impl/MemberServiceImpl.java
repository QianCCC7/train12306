package com.xiaoqian.member.service.impl;

import com.xiaoqian.common.domain.ResponseResult;
import com.xiaoqian.common.enums.HttpCodeEnum;
import com.xiaoqian.common.exception.BizException;
import com.xiaoqian.common.utils.SnowUtil;
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
            throw new BizException(HttpCodeEnum.MEMBER_MOBILE_EXIST);
        }
        Member member = new Member(SnowUtil.getSnowFlakeNextId(), memberRegisterDTO.getMobile());
        save(member);

        return ResponseResult.okResult(member.getId());
    }
}

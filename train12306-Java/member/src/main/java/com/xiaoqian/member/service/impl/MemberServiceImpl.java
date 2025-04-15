package com.xiaoqian.member.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.jwt.JWTUtil;
import com.xiaoqian.common.domain.ResponseResult;
import com.xiaoqian.common.enums.HttpCodeEnum;
import com.xiaoqian.common.exception.BizException;
import com.xiaoqian.common.utils.SnowUtil;
import com.xiaoqian.member.domain.dto.MemberLoginDTO;
import com.xiaoqian.member.domain.dto.MemberRegisterDTO;
import com.xiaoqian.member.domain.pojo.Member;
import com.xiaoqian.member.domain.vo.MemberInfoVo;
import com.xiaoqian.member.mapper.MemberMapper;
import com.xiaoqian.member.service.IMemberService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

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

    public ResponseResult<String> sendCode(MemberRegisterDTO memberRegisterDTO) {
        Member oldMember = getMemberByMoBile(memberRegisterDTO.getMobile());
        // 手机号不存在则自动注册手机号
        if (oldMember == null) {
            Member member = new Member(SnowUtil.getSnowFlakeNextId(), memberRegisterDTO.getMobile());
            save(member);
        }
        String code = RandomUtil.randomString(4);
        // TODO: 设置验证码有效期

        return ResponseResult.okResult(code);
    }

    public ResponseResult<MemberInfoVo> login(MemberLoginDTO memberLoginDTO) {
        Member oldMember = getMemberByMoBile(memberLoginDTO.getMobile());
        if (oldMember == null) {
            throw new BizException(HttpCodeEnum.MEMBER_MOBILE_NOT_EXIST);
        }
        // TODO: 修改校验验证码逻辑
        if (!memberLoginDTO.getCode().equals("8888")) {
            throw new BizException(HttpCodeEnum.CODE_ERROR);
        }
        // 生成JWT
        MemberInfoVo memberInfoVo = BeanUtil.copyProperties(oldMember, MemberInfoVo.class);
        String key = "xiaoqian666"; // 密钥
        Map<String, Object> payloadMap = BeanUtil.beanToMap(memberInfoVo);
        String token = JWTUtil.createToken(payloadMap, key.getBytes());
        memberInfoVo.setToken(token);

        return ResponseResult.okResult(memberInfoVo);
    }

    private Member getMemberByMoBile(String mobile) {
        return lambdaQuery()
                .eq(Member::getMobile, mobile)
                .one();
    }
}

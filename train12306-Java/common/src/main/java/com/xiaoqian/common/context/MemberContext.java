package com.xiaoqian.common.context;

import com.xiaoqian.common.domain.vo.MemberInfoVo;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MemberContext {
    private static final ThreadLocal<MemberInfoVo> member = new ThreadLocal<>();

    public static MemberInfoVo getMember() {
        return member.get();
    }

    public static void setMember(MemberInfoVo memberInfoVo) {
        member.set(memberInfoVo);
    }

    public static Long getId() {
        try {
            return getMember().getId();
        } catch (Exception e) {
            log.error("获取登录会员信息异常", e);
            throw e;
        }
    }
}

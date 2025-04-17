package com.xiaoqian.common.interceptor;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.xiaoqian.common.context.MemberContext;
import com.xiaoqian.common.domain.vo.MemberInfoVo;
import com.xiaoqian.common.utils.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
public class MemberInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 获取header的token参数
        String token = request.getHeader("token");
        if (StrUtil.isNotBlank(token)) {
            JSONObject loginMember = JwtUtil.getJSONObject(token);
            log.info("当前登录会员：{}", loginMember);
            MemberContext.setMember(JSONUtil.toBean(loginMember, MemberInfoVo.class));
        }
        return true;
    }
}

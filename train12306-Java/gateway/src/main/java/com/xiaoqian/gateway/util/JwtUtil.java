package com.xiaoqian.gateway.util;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.json.JSONObject;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTPayload;
import cn.hutool.jwt.JWTUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class JwtUtil {
    private final static String key = "xiaoqian666";

    public static String createToken(Long id, String mobile) {
        DateTime now = DateTime.now();
        // 有效期一天
        DateTime expTime = now.offsetNew(DateField.HOUR, 24);
        Map<String, Object> payload = new HashMap<>();
        // 签发时间
        payload.put(JWTPayload.ISSUED_AT, now);
        // 生效时间
        payload.put(JWTPayload.NOT_BEFORE, now);
        // 过期时间
        payload.put(JWTPayload.EXPIRES_AT, expTime);
        // 内容
        payload.put("id", id);
        payload.put("mobile", mobile);
        return JWTUtil.createToken(payload, key.getBytes());
    }

    public static boolean validate(String token) {
        try {
            JWT jwt = JWTUtil.parseToken(token).setKey(key.getBytes());
            // validate包含了verify
            // 第二个参数leeway – 容忍空间，单位：秒。当不能晚于当前时间时，向后容忍；不能早于向前容忍。
            return jwt.validate(0);
        } catch (Exception e) {
            return false;
        }
    }

    public static JSONObject getJSONObject(String token) {
        JWT jwt = JWTUtil.parseToken(token).setKey(key.getBytes());
        JSONObject payloads = jwt.getPayloads();
        payloads.remove(JWTPayload.ISSUED_AT);
        payloads.remove(JWTPayload.EXPIRES_AT);
        payloads.remove(JWTPayload.NOT_BEFORE);
        return payloads;
    }

    public static void main(String[] args) {
        String token = createToken(1L, "123");
        log.info("token:{}", token);
        boolean validate = validate(token);
        log.info("校验:{}", validate);
        JSONObject payload = getJSONObject(token);
        log.info("payload:{}", payload);
    }

}

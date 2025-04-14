package com.xiaoqian.common.utils;

import cn.hutool.core.util.IdUtil;

/**
 * 封装hutool雪花算法
 * 雪花算法64bit生成的ID格式为：1bit数据位 + 41bit时间戳 + 10bit工作机器ID(5bit-datacenterId和5bit-workerId) + 12bit序列号
 * 雪花算法问题及解决：
 * 1. 机器ID的设置：可以使用redis自增序列，也可以利用数据库为每台机器分配workId
 * 2. 时钟回拨：时钟回拨范围内不启动服务器
 */
public class SnowUtil {
    private static final long dataCenterId = 1;
    private static final long workerId = 1;

    public static long getSnowFlakeNextId() {
        return IdUtil.getSnowflake(workerId, dataCenterId).nextId();
    }

    public static String getSnowFlakeNextIdStr() {
        return IdUtil.getSnowflake(workerId, dataCenterId).nextIdStr();
    }
}

package com.xiaoqian.member.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.xiaoqian.common.domain.ResponseResult;
import com.xiaoqian.common.utils.SnowUtil;
import com.xiaoqian.member.domain.dto.PassengerDTO;
import com.xiaoqian.member.domain.pojo.Passenger;
import com.xiaoqian.member.mapper.PassengerMapper;
import com.xiaoqian.member.service.IPassengerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * <p>
 * 乘车人 服务实现类
 * </p>
 *
 * @author xiaoqian
 * @since 2025-04-16
 */
@Service
public class PassengerServiceImpl extends ServiceImpl<PassengerMapper, Passenger> implements IPassengerService {

    @Override
    public ResponseResult<Void> savePassenger(PassengerDTO passengerDTO) {
        Passenger passenger = BeanUtil.copyProperties(passengerDTO, Passenger.class);
        passenger.setId(SnowUtil.getSnowFlakeNextId());
        LocalDateTime now = LocalDateTime.now();
        passenger.setUpdateTime(now);
        passenger.setCreateTime(now);
        save(passenger);
        return ResponseResult.okEmptyResult();
    }
}

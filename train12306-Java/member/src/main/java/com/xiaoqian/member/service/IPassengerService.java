package com.xiaoqian.member.service;

import com.xiaoqian.common.domain.ResponseResult;
import com.xiaoqian.member.domain.dto.PassengerDTO;
import com.xiaoqian.member.domain.pojo.Passenger;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 乘车人 服务类
 * </p>
 *
 * @author xiaoqian
 * @since 2025-04-16
 */
public interface IPassengerService extends IService<Passenger> {

    ResponseResult<Void> savePassenger(PassengerDTO passengerDTO);
}

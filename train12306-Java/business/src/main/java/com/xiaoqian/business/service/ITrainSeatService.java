package com.xiaoqian.business.service;

import com.xiaoqian.business.domain.dto.TrainSeatDTO;
import com.xiaoqian.business.domain.pojo.TrainSeat;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaoqian.business.domain.query.TrainSeatQueryDTO;
import com.xiaoqian.business.domain.vo.TrainSeatVo;
import com.xiaoqian.common.domain.ResponseResult;
import com.xiaoqian.common.query.PageVo;

import java.util.List;

/**
 * <p>
 * 座位 服务类
 * </p>
 *
 * @author xiaoqian
 * @since 2025-04-20
 */
public interface ITrainSeatService extends IService<TrainSeat> {

    ResponseResult<Void> saveTrainSeat(TrainSeatDTO trainSeatDTO);

    ResponseResult<PageVo<TrainSeatVo>> listTrainSeats(TrainSeatQueryDTO queryDTO);

    ResponseResult<Void> deleteById(Long id);

    List<TrainSeat> getByTrainCode(String trainCode);
}

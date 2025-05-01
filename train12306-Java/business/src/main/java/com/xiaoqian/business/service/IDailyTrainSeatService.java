package com.xiaoqian.business.service;

import com.xiaoqian.business.domain.dto.DailyTrainSeatDTO;
import com.xiaoqian.business.domain.pojo.DailyTrainSeat;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaoqian.business.domain.query.DailyTrainSeatQueryDTO;
import com.xiaoqian.business.domain.vo.DailyTrainSeatVo;
import com.xiaoqian.common.domain.ResponseResult;
import com.xiaoqian.common.query.PageVo;

import java.time.LocalDate;
import java.util.List;

/**
 * <p>
 * 每日座位 服务类
 * </p>
 *
 * @author xiaoqian
 * @since 2025-04-24
 */
public interface IDailyTrainSeatService extends IService<DailyTrainSeat> {

    ResponseResult<Void> saveDailyTrainCarriage(DailyTrainSeatDTO dailyTrainSeatDTO);

    ResponseResult<PageVo<DailyTrainSeatVo>> listDailyTrainSeatPage(DailyTrainSeatQueryDTO queryDTO);

    ResponseResult<Void> deleteById(Long id);

    void generateDailyTrainSeat(String trainCode, LocalDate date);

    int getSeatCountByCodeAndDateAndSeatType(String trainCode, LocalDate date, String seatType);

    List<DailyTrainSeat> getListByDateAndCodeAndCarriageIndex(String trainCode, LocalDate date, Integer carriageIndex);
}

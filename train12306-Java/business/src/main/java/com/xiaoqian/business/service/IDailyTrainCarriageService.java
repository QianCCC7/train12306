package com.xiaoqian.business.service;

import com.xiaoqian.business.domain.dto.DailyTrainCarriageDTO;
import com.xiaoqian.business.domain.pojo.DailyTrainCarriage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaoqian.business.domain.query.DailyTrainCarriageQueryDTO;
import com.xiaoqian.business.domain.vo.DailyTrainCarriageVo;
import com.xiaoqian.common.domain.ResponseResult;
import com.xiaoqian.common.query.PageVo;

import java.time.LocalDate;

/**
 * <p>
 * 每日车厢 服务类
 * </p>
 *
 * @author xiaoqian
 * @since 2025-04-24
 */
public interface IDailyTrainCarriageService extends IService<DailyTrainCarriage> {

    ResponseResult<Void> saveDailyTrainCarriage(DailyTrainCarriageDTO dailyTrainCarriageDTO);

    ResponseResult<PageVo<DailyTrainCarriageVo>> listDailyTrainCarriagePage(DailyTrainCarriageQueryDTO queryDTO);

    ResponseResult<Void> deleteById(Long id);

    void generateDailyTrainCarriage(String trainCode, LocalDate date);
}

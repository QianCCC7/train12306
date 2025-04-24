package com.xiaoqian.business.service;

import com.xiaoqian.business.domain.dto.DailyTrainStationDTO;
import com.xiaoqian.business.domain.pojo.DailyTrainStation;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaoqian.business.domain.query.DailyTrainStationQueryDTO;
import com.xiaoqian.business.domain.vo.DailyTrainStationVo;
import com.xiaoqian.common.domain.ResponseResult;
import com.xiaoqian.common.query.PageVo;

/**
 * <p>
 * 每日历经车站 服务类
 * </p>
 *
 * @author xiaoqian
 * @since 2025-04-24
 */
public interface IDailyTrainStationService extends IService<DailyTrainStation> {

    ResponseResult<Void> saveDailyTrainStation(DailyTrainStationDTO dailyTrainStationDTO);

    ResponseResult<PageVo<DailyTrainStationVo>> listDailyTrainStationPage(DailyTrainStationQueryDTO queryDTO);

    ResponseResult<Void> deleteById(Long id);
}

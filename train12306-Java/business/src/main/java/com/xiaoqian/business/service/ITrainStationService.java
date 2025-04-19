package com.xiaoqian.business.service;

import com.xiaoqian.business.domain.dto.TrainStationDTO;
import com.xiaoqian.business.domain.pojo.TrainStation;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaoqian.business.domain.query.TrainStationQueryDTO;
import com.xiaoqian.business.domain.vo.TrainStationVo;
import com.xiaoqian.common.domain.ResponseResult;
import com.xiaoqian.common.query.PageVo;

/**
 * <p>
 * 车次历经车站 服务类
 * </p>
 *
 * @author xiaoqian
 * @since 2025-04-19
 */
public interface ITrainStationService extends IService<TrainStation> {

    ResponseResult<Void> saveTrainStation(TrainStationDTO trainStationDTO);

    ResponseResult<PageVo<TrainStationVo>> listTrainStations(TrainStationQueryDTO queryDTO);

    ResponseResult<Void> deleteById(Long id);
}

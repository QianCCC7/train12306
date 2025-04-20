package com.xiaoqian.business.service;

import com.xiaoqian.business.domain.dto.StationDTO;
import com.xiaoqian.business.domain.pojo.Station;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaoqian.business.domain.query.StationQueryDTO;
import com.xiaoqian.business.domain.vo.StationVo;
import com.xiaoqian.common.domain.ResponseResult;
import com.xiaoqian.common.query.PageVo;

import java.util.List;

/**
 * <p>
 * 车站 服务类
 * </p>
 *
 * @author xiaoqian
 * @since 2025-04-18
 */
public interface IStationService extends IService<Station> {

    ResponseResult<Void> saveStation(StationDTO stationDTO);

    ResponseResult<PageVo<StationVo>> listStations(StationQueryDTO queryDTO);

    ResponseResult<Void> deleteById(Long id);

    ResponseResult<List<StationVo>> getAllStations();
}

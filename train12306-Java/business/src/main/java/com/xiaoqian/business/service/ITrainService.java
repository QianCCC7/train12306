package com.xiaoqian.business.service;

import com.xiaoqian.business.domain.dto.TrainDTO;
import com.xiaoqian.business.domain.pojo.Train;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaoqian.business.domain.query.TrainQueryDTO;
import com.xiaoqian.business.domain.vo.TrainVo;
import com.xiaoqian.common.domain.ResponseResult;
import com.xiaoqian.common.query.PageVo;

import java.util.List;

/**
 * <p>
 * 车次 服务类
 * </p>
 *
 * @author xiaoqian
 * @since 2025-04-19
 */
public interface ITrainService extends IService<Train> {

    ResponseResult<Void> saveTrain(TrainDTO trainDTO);

    ResponseResult<PageVo<TrainVo>> listTrains(TrainQueryDTO queryDTO);

    ResponseResult<Void> deleteById(Long id);

    ResponseResult<List<TrainVo>> getAllTrains(TrainQueryDTO queryDTO);
}

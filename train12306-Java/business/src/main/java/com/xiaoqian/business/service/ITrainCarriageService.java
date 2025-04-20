package com.xiaoqian.business.service;

import com.xiaoqian.business.domain.dto.TrainCarriageDTO;
import com.xiaoqian.business.domain.pojo.TrainCarriage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaoqian.business.domain.query.TrainCarriageQueryDTO;
import com.xiaoqian.business.domain.vo.TrainCarriageVo;
import com.xiaoqian.common.domain.ResponseResult;
import com.xiaoqian.common.query.PageVo;

/**
 * <p>
 * 火车车厢 服务类
 * </p>
 *
 * @author xiaoqian
 * @since 2025-04-20
 */
public interface ITrainCarriageService extends IService<TrainCarriage> {

    ResponseResult<Void> saveTrainCarriage(TrainCarriageDTO trainCarriageDTO);

    ResponseResult<PageVo<TrainCarriageVo>> listTrainCarriages(TrainCarriageQueryDTO queryDTO);

    ResponseResult<Void> deleteById(Long id);
}

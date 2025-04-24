package com.xiaoqian.business.service;

import com.xiaoqian.business.domain.dto.DailyTrainDTO;
import com.xiaoqian.business.domain.pojo.DailyTrain;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaoqian.business.domain.query.DailyTrainQueryDTO;
import com.xiaoqian.business.domain.vo.DailyTrainVo;
import com.xiaoqian.common.domain.ResponseResult;
import com.xiaoqian.common.query.PageVo;

/**
 * <p>
 * 每日车次 服务类
 * </p>
 *
 * @author xiaoqian
 * @since 2025-04-24
 */
public interface IDailyTrainService extends IService<DailyTrain> {

    ResponseResult<Void> saveDailyTrain(DailyTrainDTO dailyTrainDTO);

    ResponseResult<PageVo<DailyTrainVo>> listDailyTrainPage(DailyTrainQueryDTO queryDTO);

    ResponseResult<Void> deleteById(Long id);
}

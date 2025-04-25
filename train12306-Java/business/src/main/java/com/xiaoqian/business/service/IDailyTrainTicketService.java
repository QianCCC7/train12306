package com.xiaoqian.business.service;

import com.xiaoqian.business.domain.dto.DailyTrainTicketDTO;
import com.xiaoqian.business.domain.pojo.DailyTrainTicket;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaoqian.business.domain.query.DailyTrainTicketQueryDTO;
import com.xiaoqian.business.domain.vo.DailyTrainTicketVo;
import com.xiaoqian.common.domain.ResponseResult;
import com.xiaoqian.common.query.PageVo;

/**
 * <p>
 * 余票信息 服务类
 * </p>
 *
 * @author xiaoqian
 * @since 2025-04-25
 */
public interface IDailyTrainTicketService extends IService<DailyTrainTicket> {

    ResponseResult<Void> saveDailyTrainTicket(DailyTrainTicketDTO dailyTrainTicketDTO);

    ResponseResult<PageVo<DailyTrainTicketVo>> listDailyTrainTicketPage(DailyTrainTicketQueryDTO queryDTO);

    ResponseResult<Void> deleteById(Long id);
}

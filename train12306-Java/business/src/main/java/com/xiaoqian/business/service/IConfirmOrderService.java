package com.xiaoqian.business.service;

import com.xiaoqian.business.domain.dto.ConfirmOrderDTO;
import com.xiaoqian.business.domain.pojo.ConfirmOrder;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaoqian.business.domain.query.ConfirmOrderQueryDTO;
import com.xiaoqian.business.domain.vo.ConfirmOrderVo;
import com.xiaoqian.common.domain.ResponseResult;
import com.xiaoqian.common.query.PageVo;

/**
 * <p>
 * 确认订单 服务类
 * </p>
 *
 * @author xiaoqian
 * @since 2025-04-30
 */
public interface IConfirmOrderService extends IService<ConfirmOrder> {

    ResponseResult<Void> saveOrder(ConfirmOrderDTO confirmOrderDTO);

    ResponseResult<PageVo<ConfirmOrderVo>> listOrderPage(ConfirmOrderQueryDTO confirmOrderQueryDTO);

    ResponseResult<Void> submitOrder(ConfirmOrderDTO confirmOrderDTO);
}

package com.xiaoqian.business.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaoqian.business.domain.dto.ConfirmOrderDTO;
import com.xiaoqian.business.domain.pojo.ConfirmOrder;
import com.xiaoqian.business.domain.query.ConfirmOrderQueryDTO;
import com.xiaoqian.business.domain.vo.ConfirmOrderVo;
import com.xiaoqian.business.mapper.ConfirmOrderMapper;
import com.xiaoqian.business.service.IConfirmOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaoqian.common.domain.ResponseResult;
import com.xiaoqian.common.query.PageVo;
import com.xiaoqian.common.utils.SnowUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 确认订单 服务实现类
 * </p>
 *
 * @author xiaoqian
 * @since 2025-04-30
 */
@Service
public class ConfirmOrderServiceImpl extends ServiceImpl<ConfirmOrderMapper, ConfirmOrder> implements IConfirmOrderService {

    @Override
    public ResponseResult<Void> saveOrder(ConfirmOrderDTO confirmOrderDTO) {
        ConfirmOrder confirmOrder = BeanUtil.copyProperties(confirmOrderDTO, ConfirmOrder.class);
        LocalDateTime now = LocalDateTime.now();
        if (confirmOrderDTO.getId() == null) {
            confirmOrder.setId(SnowUtil.getSnowFlakeNextId());
            confirmOrder.setCreateTime(now);
            confirmOrder.setUpdateTime(now);
            save(confirmOrder);
        } else {
            confirmOrder.setUpdateTime(now);
            updateById(confirmOrder);
        }

        return ResponseResult.okEmptyResult();
    }

    @Override
    public ResponseResult<PageVo<ConfirmOrderVo>> listOrderPage(ConfirmOrderQueryDTO query) {
        Page<ConfirmOrder> page = new Page<>(query.getPageNum(), query.getPageSize());
        page(page, new LambdaQueryWrapper<ConfirmOrder>()
                .orderByAsc(true, ConfirmOrder::getDate)
                .orderByAsc(true, ConfirmOrder::getTrainCode));
        List<ConfirmOrder> confirmOrderList = page.getRecords();
        List<ConfirmOrderVo> confirmOrderVoList = BeanUtil.copyToList(confirmOrderList, ConfirmOrderVo.class);

        return ResponseResult.okResult(new PageVo<>(confirmOrderVoList, page.getPages(), page.getTotal()));
    }
}

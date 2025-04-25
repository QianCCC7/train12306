package com.xiaoqian.business.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaoqian.business.domain.dto.DailyTrainTicketDTO;
import com.xiaoqian.business.domain.pojo.DailyTrainTicket;
import com.xiaoqian.business.domain.query.DailyTrainTicketQueryDTO;
import com.xiaoqian.business.domain.vo.DailyTrainTicketVo;
import com.xiaoqian.business.mapper.DailyTrainTicketMapper;
import com.xiaoqian.business.service.IDailyTrainTicketService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaoqian.common.domain.ResponseResult;
import com.xiaoqian.common.query.PageVo;
import com.xiaoqian.common.utils.SnowUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 余票信息 服务实现类
 * </p>
 *
 * @author xiaoqian
 * @since 2025-04-25
 */
@Service
public class DailyTrainTicketServiceImpl extends ServiceImpl<DailyTrainTicketMapper, DailyTrainTicket> implements IDailyTrainTicketService {

    @Override
    public ResponseResult<Void> saveDailyTrainTicket(DailyTrainTicketDTO dailyTrainTicketDTO) {
        DailyTrainTicket dailyTrainTicket = BeanUtil.copyProperties(dailyTrainTicketDTO, DailyTrainTicket.class);
        if (dailyTrainTicketDTO.getId() == null) {
            dailyTrainTicket.setId(SnowUtil.getSnowFlakeNextId());
            LocalDateTime now = LocalDateTime.now();
            dailyTrainTicket.setUpdateTime(now);
            dailyTrainTicket.setCreateTime(now);
            save(dailyTrainTicket);
        } else {
            dailyTrainTicket.setUpdateTime(LocalDateTime.now());
            updateById(dailyTrainTicket);
        }

        return ResponseResult.okEmptyResult();
    }

    @Override
    public ResponseResult<PageVo<DailyTrainTicketVo>> listDailyTrainTicketPage(DailyTrainTicketQueryDTO queryDTO) {
        Page<DailyTrainTicket> page = new Page<>(queryDTO.getPageNum(), queryDTO.getPageSize());
        page(page, new LambdaQueryWrapper<DailyTrainTicket>()
                .orderByDesc(true, DailyTrainTicket::getDate)
                .orderByAsc(true, DailyTrainTicket::getTrainCode)
                .orderByAsc(true, DailyTrainTicket::getStartPinyin)
                .orderByAsc(true, DailyTrainTicket::getEndPinyin));
        List<DailyTrainTicket> dailyTrainTicketList = page.getRecords();
        List<DailyTrainTicketVo> dailyTrainTicketVoList = BeanUtil.copyToList(dailyTrainTicketList, DailyTrainTicketVo.class);

        return ResponseResult.okResult(new PageVo<>(dailyTrainTicketVoList, page.getPages(), page.getTotal()));
    }

    @Override
    public ResponseResult<Void> deleteById(Long id) {
        removeById(id);
        return ResponseResult.okEmptyResult();
    }
}

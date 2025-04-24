package com.xiaoqian.business.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaoqian.business.domain.dto.DailyTrainSeatDTO;
import com.xiaoqian.business.domain.pojo.DailyTrainSeat;
import com.xiaoqian.business.domain.query.DailyTrainSeatQueryDTO;
import com.xiaoqian.business.domain.vo.DailyTrainSeatVo;
import com.xiaoqian.business.mapper.DailyTrainSeatMapper;
import com.xiaoqian.business.service.IDailyTrainSeatService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaoqian.common.domain.ResponseResult;
import com.xiaoqian.common.query.PageVo;
import com.xiaoqian.common.utils.SnowUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 每日座位 服务实现类
 * </p>
 *
 * @author xiaoqian
 * @since 2025-04-24
 */
@Service
public class DailyTrainSeatServiceImpl extends ServiceImpl<DailyTrainSeatMapper, DailyTrainSeat> implements IDailyTrainSeatService {

    @Override
    public ResponseResult<Void> saveDailyTrainCarriage(DailyTrainSeatDTO dailyTrainSeatDTO) {
        DailyTrainSeat dailyTrainSeat = BeanUtil.copyProperties(dailyTrainSeatDTO, DailyTrainSeat.class);
        if (dailyTrainSeatDTO.getId() == null) {
            dailyTrainSeat.setId(SnowUtil.getSnowFlakeNextId());
            LocalDateTime now = LocalDateTime.now();
            dailyTrainSeat.setUpdateTime(now);
            dailyTrainSeat.setCreateTime(now);
            save(dailyTrainSeat);
        } else {
            dailyTrainSeat.setUpdateTime(LocalDateTime.now());
            updateById(dailyTrainSeat);
        }

        return ResponseResult.okEmptyResult();
    }

    @Override
    public ResponseResult<PageVo<DailyTrainSeatVo>> listDailyTrainSeatPage(DailyTrainSeatQueryDTO queryDTO) {
        Page<DailyTrainSeat> page = new Page<>(queryDTO.getPageNum(), queryDTO.getPageSize());
        page(page, new LambdaQueryWrapper<DailyTrainSeat>()
                .eq(StringUtils.hasText(queryDTO.getTrainCode()), DailyTrainSeat::getTrainCode, queryDTO.getTrainCode())
                .orderByDesc(true, DailyTrainSeat::getDate)
                .orderByAsc(true, DailyTrainSeat::getTrainCode)
                .orderByAsc(true, DailyTrainSeat::getCarriageIndex)
                .orderByAsc(true, DailyTrainSeat::getCarriageSeatIndex));
        List<DailyTrainSeat> dailyTrainSeatList = page.getRecords();
        List<DailyTrainSeatVo> dailyTrainSeatVoList = BeanUtil.copyToList(dailyTrainSeatList, DailyTrainSeatVo.class);

        return ResponseResult.okResult(new PageVo<>(dailyTrainSeatVoList, page.getPages(), page.getTotal()));
    }

    @Override
    public ResponseResult<Void> deleteById(Long id) {
        removeById(id);
        return ResponseResult.okEmptyResult();
    }
}

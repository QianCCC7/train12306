package com.xiaoqian.business.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaoqian.business.domain.dto.DailyTrainSeatDTO;
import com.xiaoqian.business.domain.pojo.*;
import com.xiaoqian.business.domain.query.DailyTrainSeatQueryDTO;
import com.xiaoqian.business.domain.vo.DailyTrainSeatVo;
import com.xiaoqian.business.mapper.DailyTrainSeatMapper;
import com.xiaoqian.business.service.IDailyTrainSeatService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaoqian.business.service.ITrainSeatService;
import com.xiaoqian.business.service.ITrainStationService;
import com.xiaoqian.common.domain.ResponseResult;
import com.xiaoqian.common.query.PageVo;
import com.xiaoqian.common.utils.SnowUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
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
@RequiredArgsConstructor
public class DailyTrainSeatServiceImpl extends ServiceImpl<DailyTrainSeatMapper, DailyTrainSeat> implements IDailyTrainSeatService {
    private final ITrainSeatService trainSeatService;
    private final ITrainStationService trainStationService;

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

    /**
     * 生成车次的座位信息
     */
    public void generateDailyTrainSeat(String trainCode, LocalDate date) {
        // 删除date天车次的座位信息
        remove(new LambdaQueryWrapper<DailyTrainSeat>()
                .eq(DailyTrainSeat::getTrainCode, trainCode)
                .eq(DailyTrainSeat::getDate, date));
        // 查询车次的所有座位
        List<TrainSeat> trainSeatList = trainSeatService.getByTrainCode(trainCode);
        if (CollectionUtils.isEmpty(trainSeatList)) {
            log.debug("车次无车座信息");
            return;
        }
        // 查询当前车次历经多少车站
        List<TrainStation> trainStationList = trainStationService.getByCode(trainCode);
        String sell = StrUtil.fillBefore("", '0', trainStationList.size() - 1);
        for (TrainSeat trainSeat : trainSeatList) {
            DailyTrainSeat dailyTrainSeat = BeanUtil.copyProperties(trainSeat, DailyTrainSeat.class);
            dailyTrainSeat.setId(SnowUtil.getSnowFlakeNextId());
            dailyTrainSeat.setDate(date);
            LocalDateTime now = LocalDateTime.now();
            dailyTrainSeat.setCreateTime(now);
            dailyTrainSeat.setUpdateTime(now);
            dailyTrainSeat.setSell(sell);
            save(dailyTrainSeat);
        }
    }

    @Override
    public int getSeatCountByCodeAndDateAndSeatType(String trainCode, LocalDate date, String seatType) {
        int count = lambdaQuery().eq(DailyTrainSeat::getTrainCode, trainCode)
                .eq(DailyTrainSeat::getDate, date)
                .eq(DailyTrainSeat::getSeatType, seatType)
                .count().intValue();
        return count == 0 ? -1 : count;
    }

    @Override
    public List<DailyTrainSeat> getListByDateAndCodeAndCarriageIndex(String trainCode, LocalDate date, Integer carriageIndex) {
        return lambdaQuery().eq(DailyTrainSeat::getTrainCode, trainCode)
                .eq(DailyTrainSeat::getDate, date)
                .eq(DailyTrainSeat::getCarriageIndex, carriageIndex)
                .orderByAsc(true, DailyTrainSeat::getCarriageSeatIndex)
                .list();
    }
}

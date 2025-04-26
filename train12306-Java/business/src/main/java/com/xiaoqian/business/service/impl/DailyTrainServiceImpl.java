package com.xiaoqian.business.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaoqian.business.domain.dto.DailyTrainDTO;
import com.xiaoqian.business.domain.pojo.DailyTrain;
import com.xiaoqian.business.domain.pojo.Train;
import com.xiaoqian.business.domain.query.DailyTrainQueryDTO;
import com.xiaoqian.business.domain.vo.DailyTrainVo;
import com.xiaoqian.business.mapper.DailyTrainMapper;
import com.xiaoqian.business.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaoqian.common.domain.ResponseResult;
import com.xiaoqian.common.enums.HttpCodeEnum;
import com.xiaoqian.common.exception.BizException;
import com.xiaoqian.common.query.PageVo;
import com.xiaoqian.common.utils.SnowUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 每日车次 服务实现类
 * </p>
 *
 * @author xiaoqian
 * @since 2025-04-24
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DailyTrainServiceImpl extends ServiceImpl<DailyTrainMapper, DailyTrain> implements IDailyTrainService {
    private final ITrainService trainService;
    private final IDailyTrainStationService dailyTrainStationService;
    private final IDailyTrainCarriageService dailyTrainCarriageService;
    private final IDailyTrainSeatService dailyTrainSeatService;
    private final IDailyTrainTicketService dailyTrainTicketService;

    @Override
    public ResponseResult<Void> saveDailyTrain(DailyTrainDTO dailyTrainDTO) {
        DailyTrain dailyTrain = getByTrainCode(dailyTrainDTO.getCode());
        if (dailyTrainDTO.getId() == null) {
            if (dailyTrain != null) {
                throw new BizException(HttpCodeEnum.TRAIN_CODE_EXIST);
            }
            dailyTrain = BeanUtil.copyProperties(dailyTrainDTO, DailyTrain.class);
            dailyTrain.setId(SnowUtil.getSnowFlakeNextId());
            LocalDateTime now = LocalDateTime.now();
            dailyTrain.setUpdateTime(now);
            dailyTrain.setCreateTime(now);
            save(dailyTrain);
        } else {
            DailyTrain data = lambdaQuery().eq(DailyTrain::getId, dailyTrainDTO.getId()).one();
            if (data != null && !data.getCode().equals(dailyTrainDTO.getCode())) {
                if (dailyTrain != null) {
                    throw new BizException(HttpCodeEnum.TRAIN_CODE_EXIST);
                }
            }
            dailyTrain = BeanUtil.copyProperties(dailyTrainDTO, DailyTrain.class);
            dailyTrain.setUpdateTime(LocalDateTime.now());
            updateById(dailyTrain);
        }

        return ResponseResult.okEmptyResult();
    }

    private DailyTrain getByTrainCode(String trainCode) {
        return lambdaQuery().eq(DailyTrain::getCode, trainCode).one();
    }

    @Override
    public ResponseResult<PageVo<DailyTrainVo>> listDailyTrainPage(DailyTrainQueryDTO queryDTO) {
        Page<DailyTrain> page = new Page<>(queryDTO.getPageNum(), queryDTO.getPageSize());
        page(page, new LambdaQueryWrapper<DailyTrain>()
                .eq(StringUtils.hasText(queryDTO.getCode()), DailyTrain::getCode, queryDTO.getCode())
                .eq(queryDTO.getDate() != null, DailyTrain::getDate, queryDTO.getDate())
                .orderByDesc(true, DailyTrain::getDate)
                .orderByAsc(true, DailyTrain::getCode));
        List<DailyTrain> dailyTrainList = page.getRecords();
        List<DailyTrainVo> dailyTrainVoList = BeanUtil.copyToList(dailyTrainList, DailyTrainVo.class);

        return ResponseResult.okResult(new PageVo<>(dailyTrainVoList, page.getPages(), page.getTotal()));
    }

    @Override
    public ResponseResult<Void> deleteById(Long id) {
        removeById(id);
        return ResponseResult.okEmptyResult();
    }

    /**
     * 生成某日所有车次信息
     */
    @Override
    @Transactional
    public ResponseResult<Void> generateDailyTrain(LocalDate date) {
        // 所有车次信息
        List<Train> trainList = trainService.lambdaQuery().list();
        if (CollectionUtils.isEmpty(trainList)) {
            log.debug("暂无车次信息");
            return ResponseResult.okEmptyResult();
        }
        for (Train train : trainList) {
            genDailyTrain(train, date);
        }

        return ResponseResult.okEmptyResult();
    }

    private void genDailyTrain(Train train, LocalDate date) {
        // 清空在date天的车次数据
        remove(new LambdaQueryWrapper<DailyTrain>()
                .eq(DailyTrain::getCode, train.getCode())
                .eq(DailyTrain::getDate, date));
        // 重新生成date天的车次数据
        DailyTrain dailyTrain = BeanUtil.copyProperties(train, DailyTrain.class);
        dailyTrain.setId(SnowUtil.getSnowFlakeNextId());
        LocalDateTime now = LocalDateTime.now();
        dailyTrain.setUpdateTime(now);
        dailyTrain.setCreateTime(now);
        dailyTrain.setDate(date);
        save(dailyTrain);

        // 生成车次历经车站信息
        dailyTrainStationService.generateDailyTrainStation(train.getCode(), date);
        // 生成车次车厢信息
        dailyTrainCarriageService.generateDailyTrainCarriage(train.getCode(), date);
        // 生成车次座位信息
        dailyTrainSeatService.generateDailyTrainSeat(train.getCode(), date);
        // 生成车次车票信息
        dailyTrainTicketService.generateDailyTrainTicket(train.getCode(), date, dailyTrain);
    }
}

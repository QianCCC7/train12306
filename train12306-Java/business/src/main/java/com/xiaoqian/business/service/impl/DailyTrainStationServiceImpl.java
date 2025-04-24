package com.xiaoqian.business.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaoqian.business.domain.dto.DailyTrainStationDTO;
import com.xiaoqian.business.domain.pojo.DailyTrainStation;
import com.xiaoqian.business.domain.pojo.TrainStation;
import com.xiaoqian.business.domain.query.DailyTrainStationQueryDTO;
import com.xiaoqian.business.domain.vo.DailyTrainStationVo;
import com.xiaoqian.business.mapper.DailyTrainStationMapper;
import com.xiaoqian.business.service.IDailyTrainStationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
 * 每日历经车站 服务实现类
 * </p>
 *
 * @author xiaoqian
 * @since 2025-04-24
 */
@Service
@RequiredArgsConstructor
public class DailyTrainStationServiceImpl extends ServiceImpl<DailyTrainStationMapper, DailyTrainStation> implements IDailyTrainStationService {
    private final ITrainStationService trainStationService;

    @Override
    public ResponseResult<Void> saveDailyTrainStation(DailyTrainStationDTO dailyTrainStationDTO) {
        DailyTrainStation dailyTrainStation = BeanUtil.copyProperties(dailyTrainStationDTO, DailyTrainStation.class);
        if (dailyTrainStationDTO.getId() == null) {
            dailyTrainStation.setId(SnowUtil.getSnowFlakeNextId());
            LocalDateTime now = LocalDateTime.now();
            dailyTrainStation.setUpdateTime(now);
            dailyTrainStation.setCreateTime(now);
            save(dailyTrainStation);
        } else {
            dailyTrainStation.setUpdateTime(LocalDateTime.now());
            updateById(dailyTrainStation);
        }

        return ResponseResult.okEmptyResult();
    }

    @Override
    public ResponseResult<PageVo<DailyTrainStationVo>> listDailyTrainStationPage(DailyTrainStationQueryDTO queryDTO) {
        Page<DailyTrainStation> page = new Page<>(queryDTO.getPageNum(), queryDTO.getPageSize());
        page(page, new LambdaQueryWrapper<DailyTrainStation>()
                .eq(StringUtils.hasText(queryDTO.getCode()), DailyTrainStation::getTrainCode, queryDTO.getCode())
                .eq(queryDTO.getDate() != null, DailyTrainStation::getDate, queryDTO.getDate())
                .orderByDesc(true, DailyTrainStation::getDate)
                .orderByAsc(true, DailyTrainStation::getTrainCode)
                .orderByAsc(true, DailyTrainStation::getIndexOrder)
                .orderByAsc(true, DailyTrainStation::getNamePinyin));
        List<DailyTrainStation> dailyTrainStationList = page.getRecords();
        List<DailyTrainStationVo> dailyTrainVoList = BeanUtil.copyToList(dailyTrainStationList, DailyTrainStationVo.class);

        return ResponseResult.okResult(new PageVo<>(dailyTrainVoList, page.getPages(), page.getTotal()));
    }

    @Override
    public ResponseResult<Void> deleteById(Long id) {
        removeById(id);
        return ResponseResult.okEmptyResult();
    }

    /**
     * 生成某日某车次历经车站信息
     */
    @Override
    public void generateDailyTrainStation(String trainCode, LocalDate date) {
        // 删除date天车次的信息
        remove(new LambdaQueryWrapper<DailyTrainStation>()
                .eq(DailyTrainStation::getTrainCode, trainCode)
                .eq(DailyTrainStation::getDate, date));
        // 查询车次历经的所有车站
        List<TrainStation> trainStationList = trainStationService.getByCode(trainCode);
        if (CollectionUtils.isEmpty(trainStationList)) {
            log.debug("车次无历经车站信息");
            return;
        }
        for (TrainStation trainStation : trainStationList) {
            DailyTrainStation dailyTrainStation = BeanUtil.copyProperties(trainStation, DailyTrainStation.class);
            dailyTrainStation.setId(SnowUtil.getSnowFlakeNextId());
            dailyTrainStation.setDate(date);
            LocalDateTime now = LocalDateTime.now();
            dailyTrainStation.setCreateTime(now);
            dailyTrainStation.setUpdateTime(now);
            save(dailyTrainStation);
        }
    }
}

package com.xiaoqian.business.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.EnumUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaoqian.business.domain.dto.DailyTrainTicketDTO;
import com.xiaoqian.business.domain.pojo.DailyTrain;
import com.xiaoqian.business.domain.pojo.DailyTrainTicket;
import com.xiaoqian.business.domain.pojo.TrainStation;
import com.xiaoqian.business.domain.query.DailyTrainTicketQueryDTO;
import com.xiaoqian.business.domain.vo.DailyTrainTicketVo;
import com.xiaoqian.business.enums.SeatTypeEnum;
import com.xiaoqian.business.enums.TrainTypeEnum;
import com.xiaoqian.business.mapper.DailyTrainTicketMapper;
import com.xiaoqian.business.service.IDailyTrainSeatService;
import com.xiaoqian.business.service.IDailyTrainTicketService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaoqian.business.service.ITrainStationService;
import com.xiaoqian.common.domain.ResponseResult;
import com.xiaoqian.common.query.PageVo;
import com.xiaoqian.common.utils.SnowUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
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
@RequiredArgsConstructor
public class DailyTrainTicketServiceImpl extends ServiceImpl<DailyTrainTicketMapper, DailyTrainTicket> implements IDailyTrainTicketService {
    private final ITrainStationService trainStationService;
    private final IDailyTrainSeatService dailyTrainSeatService;

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
                .eq(StringUtils.hasText(queryDTO.getCode()), DailyTrainTicket::getTrainCode, queryDTO.getCode())
                .eq(queryDTO.getDate() != null, DailyTrainTicket::getDate, queryDTO.getDate())
                .eq(StringUtils.hasText(queryDTO.getStart()), DailyTrainTicket::getStart, queryDTO.getStart())
                .eq(StringUtils.hasText(queryDTO.getEnd()), DailyTrainTicket::getEnd, queryDTO.getEnd())
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

    /**
     * 生成车次某天的车票信息
     */
    @Override
    public void generateDailyTrainTicket(String trainCode, LocalDate date, DailyTrain dailyTrain) {
        // 清空在date天的车票数据
        remove(new LambdaQueryWrapper<DailyTrainTicket>()
                .eq(DailyTrainTicket::getTrainCode, trainCode)
                .eq(DailyTrainTicket::getDate, date));
        // 查询途径的车站信息
        List<TrainStation> trainStationList = trainStationService.getByCode(trainCode);
        if (CollectionUtils.isEmpty(trainStationList)) {
            log.debug("车次无历经车站信息");
            return;
        }
        String trainType = dailyTrain.getType();
        LocalDateTime now = LocalDateTime.now();
        for (int i = 0; i < trainStationList.size(); i++) {
            TrainStation start = trainStationList.get(i);
            BigDecimal totalKm = BigDecimal.ZERO;
            for (int j = i + 1; j < trainStationList.size(); j++) {
                TrainStation end = trainStationList.get(j);
                totalKm = totalKm.add(end.getKm());
                int ydzCount = dailyTrainSeatService.getSeatCountByCodeAndDateAndSeatType(trainCode, date, SeatTypeEnum.YDZ.getCode());
                int edzCount = dailyTrainSeatService.getSeatCountByCodeAndDateAndSeatType(trainCode, date, SeatTypeEnum.EDZ.getCode());
                int rwCount = dailyTrainSeatService.getSeatCountByCodeAndDateAndSeatType(trainCode, date, SeatTypeEnum.RW.getCode());
                int ywCount = dailyTrainSeatService.getSeatCountByCodeAndDateAndSeatType(trainCode, date, SeatTypeEnum.YW.getCode());
                // 票价 = TrainTypeEnum.priceRate * SeatTypeEnum.price * TrainStation.km
                BigDecimal priceRate = EnumUtil.getFieldBy(TrainTypeEnum::getPriceRate, TrainTypeEnum::getCode, trainType);
                // 得到票价并且保留两位小数
                BigDecimal ydzPrice = priceRate.multiply(SeatTypeEnum.YDZ.getPrice()).multiply(totalKm).setScale(2, RoundingMode.HALF_UP);
                BigDecimal edzPrice = priceRate.multiply(SeatTypeEnum.EDZ.getPrice()).multiply(totalKm).setScale(2, RoundingMode.HALF_UP);
                BigDecimal rwPrice = priceRate.multiply(SeatTypeEnum.RW.getPrice()).multiply(totalKm).setScale(2, RoundingMode.HALF_UP);
                BigDecimal ywPrice = priceRate.multiply(SeatTypeEnum.YW.getPrice()).multiply(totalKm).setScale(2, RoundingMode.HALF_UP);
                DailyTrainTicket dailyTrainTicket = new DailyTrainTicket(SnowUtil.getSnowFlakeNextId(), date, trainCode,
                        start.getName(), start.getNamePinyin(), start.getOutTime(), start.getIndexOrder(),
                        end.getName(), end.getNamePinyin(), end.getInTime(), end.getIndexOrder(),
                        ydzCount, ydzPrice, edzCount, edzPrice, rwCount, rwPrice, ywCount, ywPrice,
                        now, now);
                save(dailyTrainTicket);
            }
        }
    }

    @Override
    public DailyTrainTicket geyByDateAndCodeAndStartAndEnd(LocalDate date, String code, String start, String end) {
        return lambdaQuery().eq(DailyTrainTicket::getDate, date)
                .eq(DailyTrainTicket::getTrainCode, code)
                .eq(DailyTrainTicket::getStart, start)
                .eq(DailyTrainTicket::getEnd, end)
                .one();
    }
}

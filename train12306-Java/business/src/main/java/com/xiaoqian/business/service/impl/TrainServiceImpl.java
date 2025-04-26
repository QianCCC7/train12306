package com.xiaoqian.business.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaoqian.business.domain.dto.TrainDTO;
import com.xiaoqian.business.domain.pojo.Train;
import com.xiaoqian.business.domain.pojo.TrainCarriage;
import com.xiaoqian.business.domain.pojo.TrainSeat;
import com.xiaoqian.business.domain.query.TrainQueryDTO;
import com.xiaoqian.business.domain.vo.TrainVo;
import com.xiaoqian.business.enums.SeatColEnum;
import com.xiaoqian.business.enums.SeatTypeEnum;
import com.xiaoqian.business.mapper.TrainMapper;
import com.xiaoqian.business.service.ITrainCarriageService;
import com.xiaoqian.business.service.ITrainSeatService;
import com.xiaoqian.business.service.ITrainService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaoqian.common.domain.ResponseResult;
import com.xiaoqian.common.enums.HttpCodeEnum;
import com.xiaoqian.common.exception.BizException;
import com.xiaoqian.common.query.PageVo;
import com.xiaoqian.common.utils.SnowUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 车次 服务实现类
 * </p>
 *
 * @author xiaoqian
 * @since 2025-04-19
 */
@Service
@RequiredArgsConstructor
public class TrainServiceImpl extends ServiceImpl<TrainMapper, Train> implements ITrainService {
    private final ITrainCarriageService carriageService;
    private final ITrainSeatService trainSeatService;

    @Override
    public ResponseResult<Void> saveTrain(TrainDTO trainDTO) {
        Train train = getByTrainCode(trainDTO.getCode());
        if (trainDTO.getId() == null) {
            if (train != null) {
                throw new BizException(HttpCodeEnum.TRAIN_CODE_EXIST);
            }
            train = BeanUtil.copyProperties(trainDTO, Train.class);
            train.setId(SnowUtil.getSnowFlakeNextId());
            LocalDateTime now = LocalDateTime.now();
            train.setUpdateTime(now);
            train.setCreateTime(now);
            save(train);
        } else {
            Train data = lambdaQuery().eq(Train::getId, trainDTO.getId()).one();
            if (data != null && !data.getCode().equals(trainDTO.getCode())) {
                if (train != null) {
                    throw new BizException(HttpCodeEnum.TRAIN_CODE_EXIST);
                }
            }
            train = BeanUtil.copyProperties(trainDTO, Train.class);
            train.setUpdateTime(LocalDateTime.now());
            updateById(train);
        }

        return ResponseResult.okEmptyResult();
    }

    private Train getByTrainCode(String trainCode) {
        return lambdaQuery().eq(Train::getCode, trainCode).one();
    }

    @Override
    public ResponseResult<PageVo<TrainVo>> listTrains(TrainQueryDTO query) {
        Page<Train> page = new Page<>(query.getPageNum(), query.getPageSize());
        page(page, new LambdaQueryWrapper<Train>()
                .orderByAsc(true, Train::getId));
        List<Train> trainList = page.getRecords();
        List<TrainVo> trainVoList = BeanUtil.copyToList(trainList, TrainVo.class);

        return ResponseResult.okResult(new PageVo<>(trainVoList, page.getPages(), page.getTotal()));
    }

    @Override
    public ResponseResult<List<TrainVo>> getAllTrains() {
        List<Train> trainList = lambdaQuery().orderByAsc(Train::getCode).list();
        List<TrainVo> trainVoList = BeanUtil.copyToList(trainList, TrainVo.class);

        return ResponseResult.okResult(trainVoList);
    }

    @Override
    public ResponseResult<Void> deleteById(Long id) {
        removeById(id);
        return ResponseResult.okEmptyResult();
    }

    // 生成车座位数据
    @Override
    @Transactional
    public ResponseResult<Void> generateTrainSeats(String trainCode) {
        LocalDateTime now = LocalDateTime.now();
        // 删除当前车次
//        remove(new LambdaQueryWrapper<Train>().eq(StringUtils.hasText(trainCode), Train::getCode, trainCode));
        // 查找当前车次所有车厢
        List<TrainCarriage> trainCarriageList = carriageService.selectByTrainCode(trainCode);
        // 为每节车厢生成座位
        for (TrainCarriage trainCarriage : trainCarriageList) {
            int seatIndex = 1;
            // 获取车厢数据：行数、座位类型
            Integer rowCount = trainCarriage.getRowCount();
            SeatTypeEnum seatType = trainCarriage.getSeatType();
            // 根据座位类型，获取所有列
            List<SeatColEnum> seatColEnumList = SeatColEnum.getColsByType(seatType.getCode());
            // 处理每行
            for (int i = 1; i <= rowCount; i++) {
                // 处理每列
                for (SeatColEnum seatColEnum : seatColEnumList) {
                    // 保存至车座数据库表
                    TrainSeat trainSeat = new TrainSeat(SnowUtil.getSnowFlakeNextId(), trainCode, trainCarriage.getIndexOrder(),
                            StrUtil.fillBefore(String.valueOf(i), '0', 2), seatColEnum, seatType,
                            seatIndex++, now, now);
                    trainSeatService.save(trainSeat);
                }
            }
        }

        return ResponseResult.okEmptyResult();
    }
}

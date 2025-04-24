package com.xiaoqian.business.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaoqian.business.domain.dto.TrainStationDTO;
import com.xiaoqian.business.domain.pojo.TrainStation;
import com.xiaoqian.business.domain.query.TrainStationQueryDTO;
import com.xiaoqian.business.domain.vo.TrainStationVo;
import com.xiaoqian.business.mapper.TrainStationMapper;
import com.xiaoqian.business.service.ITrainStationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaoqian.common.domain.ResponseResult;
import com.xiaoqian.common.enums.HttpCodeEnum;
import com.xiaoqian.common.exception.BizException;
import com.xiaoqian.common.query.PageVo;
import com.xiaoqian.common.utils.SnowUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 车次历经车站 服务实现类
 * </p>
 *
 * @author xiaoqian
 * @since 2025-04-19
 */
@Service
public class TrainStationServiceImpl extends ServiceImpl<TrainStationMapper, TrainStation> implements ITrainStationService {

    @Override
    public ResponseResult<Void> saveTrainStation(TrainStationDTO trainStationDTO) {
        TrainStation trainStation = getByCodeAndIndexOrder(trainStationDTO.getTrainCode(), trainStationDTO.getIndexOrder());
        if (trainStation != null) {
            throw new BizException(HttpCodeEnum.TRAIN_STATION_CODE_INDEX_EXIST);
        }
        trainStation = getByCodeAndName(trainStationDTO.getTrainCode(), trainStationDTO.getName());
        if (trainStation != null) {
            throw new BizException(HttpCodeEnum.TRAIN_STATION_CODE_NAME_EXIST);
        }
        trainStation = BeanUtil.copyProperties(trainStationDTO, TrainStation.class);
        if (trainStationDTO.getId() == null) {
            trainStation.setId(SnowUtil.getSnowFlakeNextId());
            LocalDateTime now = LocalDateTime.now();
            trainStation.setUpdateTime(now);
            trainStation.setCreateTime(now);
            save(trainStation);
        } else {
            trainStation.setUpdateTime(LocalDateTime.now());
            updateById(trainStation);
        }

        return ResponseResult.okEmptyResult();
    }

    private TrainStation getByCodeAndIndexOrder(String trainCode, Integer indexOrder) {
        return lambdaQuery().eq(TrainStation::getTrainCode, trainCode)
                .eq(TrainStation::getIndexOrder, indexOrder).one();
    }

    private TrainStation getByCodeAndName(String trainCode, String name) {
        return lambdaQuery().eq(TrainStation::getTrainCode, trainCode)
                .eq(TrainStation::getName, name).one();
    }

    @Override
    public ResponseResult<PageVo<TrainStationVo>> listTrainStations(TrainStationQueryDTO query) {
        Page<TrainStation> page = new Page<>(query.getPageNum(), query.getPageSize());
        LambdaQueryWrapper<TrainStation> queryWrapper = new LambdaQueryWrapper<TrainStation>()
                .eq(StringUtils.hasText(query.getTrainCode()), TrainStation::getTrainCode, query.getTrainCode())
                .orderByAsc(true, TrainStation::getTrainCode)
                .orderByAsc(true, TrainStation::getIndexOrder);

        page(page, queryWrapper);
        List<TrainStation> trainStationList = page.getRecords();
        List<TrainStationVo> trainStationVoList = BeanUtil.copyToList(trainStationList, TrainStationVo.class);

        return ResponseResult.okResult(new PageVo<>(trainStationVoList, page.getPages(), page.getTotal()));
    }

    @Override
    public ResponseResult<Void> deleteById(Long id) {
        removeById(id);
        return ResponseResult.okEmptyResult();
    }

    public List<TrainStation> getByCode(String code) {
        return lambdaQuery().eq(TrainStation::getTrainCode, code).list();
    }
}

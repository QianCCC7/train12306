package com.xiaoqian.business.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaoqian.business.domain.dto.TrainSeatDTO;
import com.xiaoqian.business.domain.pojo.TrainSeat;
import com.xiaoqian.business.domain.query.TrainSeatQueryDTO;
import com.xiaoqian.business.domain.vo.TrainSeatVo;
import com.xiaoqian.business.mapper.TrainSeatMapper;
import com.xiaoqian.business.service.ITrainSeatService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaoqian.common.domain.ResponseResult;
import com.xiaoqian.common.query.PageVo;
import com.xiaoqian.common.utils.SnowUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 座位 服务实现类
 * </p>
 *
 * @author xiaoqian
 * @since 2025-04-20
 */
@Service
public class TrainSeatServiceImpl extends ServiceImpl<TrainSeatMapper, TrainSeat> implements ITrainSeatService {

    @Override
    public ResponseResult<Void> saveTrainSeat(TrainSeatDTO trainSeatDTO) {
        TrainSeat trainSeat = BeanUtil.copyProperties(trainSeatDTO, TrainSeat.class);
        if (trainSeatDTO.getId() == null) {
            trainSeat.setId(SnowUtil.getSnowFlakeNextId());
            LocalDateTime now = LocalDateTime.now();
            trainSeat.setUpdateTime(now);
            trainSeat.setCreateTime(now);
            save(trainSeat);
        } else {
            trainSeat.setUpdateTime(LocalDateTime.now());
            updateById(trainSeat);
        }

        return ResponseResult.okEmptyResult();
    }

    @Override
    public ResponseResult<PageVo<TrainSeatVo>> listTrainSeats(TrainSeatQueryDTO query) {
        Page<TrainSeat> page = new Page<>(query.getPageNum(), query.getPageSize());
        page(page, new LambdaQueryWrapper<TrainSeat>()
                .orderByAsc(true, TrainSeat::getId));
        List<TrainSeat> trainSeatList = page.getRecords();
        List<TrainSeatVo> trainSeatVoList = BeanUtil.copyToList(trainSeatList, TrainSeatVo.class);

        return ResponseResult.okResult(new PageVo<>(trainSeatVoList, page.getPages(), page.getTotal()));
    }

    @Override
    public ResponseResult<Void> deleteById(Long id) {
        removeById(id);
        return ResponseResult.okEmptyResult();
    }
}

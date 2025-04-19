package com.xiaoqian.business.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaoqian.business.domain.dto.TrainDTO;
import com.xiaoqian.business.domain.pojo.Train;
import com.xiaoqian.business.domain.query.TrainQueryDTO;
import com.xiaoqian.business.domain.vo.TrainVo;
import com.xiaoqian.business.mapper.TrainMapper;
import com.xiaoqian.business.service.ITrainService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaoqian.common.domain.ResponseResult;
import com.xiaoqian.common.query.PageVo;
import com.xiaoqian.common.utils.SnowUtil;
import org.springframework.stereotype.Service;

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
public class TrainServiceImpl extends ServiceImpl<TrainMapper, Train> implements ITrainService {

    @Override
    public ResponseResult<Void> saveTrain(TrainDTO trainDTO) {
        Train train = BeanUtil.copyProperties(trainDTO, Train.class);
        if (trainDTO.getId() == null) {
            train.setId(SnowUtil.getSnowFlakeNextId());
            LocalDateTime now = LocalDateTime.now();
            train.setUpdateTime(now);
            train.setCreateTime(now);
            save(train);
        } else {
            train.setUpdateTime(LocalDateTime.now());
            updateById(train);
        }

        return ResponseResult.okEmptyResult();
    }

    @Override
    public ResponseResult<PageVo<TrainVo>> listTrains(TrainQueryDTO query) {
        Page<Train> page = new Page<>(query.getPageNum(), query.getPageSize());
        page(page, new LambdaQueryWrapper<Train>()
                .orderByAsc(true, Train::getId));
        List<Train> passengerList =page.getRecords();
        List<TrainVo> passengerVoList = BeanUtil.copyToList(passengerList, TrainVo.class);

        return ResponseResult.okResult(new PageVo<>(passengerVoList, page.getPages(), page.getTotal()));
    }

    @Override
    public ResponseResult<Void> deleteById(Long id) {
        removeById(id);
        return ResponseResult.okEmptyResult();
    }
}

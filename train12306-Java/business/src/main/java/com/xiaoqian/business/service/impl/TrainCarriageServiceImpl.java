package com.xiaoqian.business.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaoqian.business.domain.dto.TrainCarriageDTO;
import com.xiaoqian.business.domain.pojo.TrainCarriage;
import com.xiaoqian.business.domain.query.TrainCarriageQueryDTO;
import com.xiaoqian.business.domain.vo.TrainCarriageVo;
import com.xiaoqian.business.mapper.TrainCarriageMapper;
import com.xiaoqian.business.service.ITrainCarriageService;
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
 * 火车车厢 服务实现类
 * </p>
 *
 * @author xiaoqian
 * @since 2025-04-20
 */
@Service
public class TrainCarriageServiceImpl extends ServiceImpl<TrainCarriageMapper, TrainCarriage> implements ITrainCarriageService {

    @Override
    public ResponseResult<Void> saveTrainCarriage(TrainCarriageDTO trainCarriageDTO) {
        TrainCarriage trainCarriage = BeanUtil.copyProperties(trainCarriageDTO, TrainCarriage.class);
        if (trainCarriageDTO.getId() == null) {
            trainCarriage.setId(SnowUtil.getSnowFlakeNextId());
            LocalDateTime now = LocalDateTime.now();
            trainCarriage.setUpdateTime(now);
            trainCarriage.setCreateTime(now);
            save(trainCarriage);
        } else {
            trainCarriage.setUpdateTime(LocalDateTime.now());
            updateById(trainCarriage);
        }

        return ResponseResult.okEmptyResult();
    }

    @Override
    public ResponseResult<PageVo<TrainCarriageVo>> listTrainCarriages(TrainCarriageQueryDTO query) {
        Page<TrainCarriage> page = new Page<>(query.getPageNum(), query.getPageSize());
        LambdaQueryWrapper<TrainCarriage> queryWrapper = new LambdaQueryWrapper<TrainCarriage>()
                .eq(StringUtils.hasText(query.getTrainCode()), TrainCarriage::getTrainCode, query.getTrainCode())
                .orderByAsc(true, TrainCarriage::getTrainCode)
                .orderByAsc(true, TrainCarriage::getIndexOrder);
        page(page, queryWrapper);
        List<TrainCarriage> trainCarriageList = page.getRecords();
        List<TrainCarriageVo> trainCarriageVoList = BeanUtil.copyToList(trainCarriageList, TrainCarriageVo.class);

        return ResponseResult.okResult(new PageVo<>(trainCarriageVoList, page.getPages(), page.getTotal()));
    }

    @Override
    public ResponseResult<Void> deleteById(Long id) {
        removeById(id);
        return ResponseResult.okEmptyResult();
    }

    public List<TrainCarriage> selectByTrainCode(String trainCode) {
        return lambdaQuery().eq(TrainCarriage::getTrainCode, trainCode).list();
    }
}

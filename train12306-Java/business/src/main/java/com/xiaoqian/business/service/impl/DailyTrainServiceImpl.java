package com.xiaoqian.business.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaoqian.business.domain.dto.DailyTrainDTO;
import com.xiaoqian.business.domain.pojo.DailyTrain;
import com.xiaoqian.business.domain.query.DailyTrainQueryDTO;
import com.xiaoqian.business.domain.vo.DailyTrainVo;
import com.xiaoqian.business.mapper.DailyTrainMapper;
import com.xiaoqian.business.service.IDailyTrainService;
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
 * 每日车次 服务实现类
 * </p>
 *
 * @author xiaoqian
 * @since 2025-04-24
 */
@Service
public class DailyTrainServiceImpl extends ServiceImpl<DailyTrainMapper, DailyTrain> implements IDailyTrainService {

    @Override
    public ResponseResult<Void> saveDailyTrain(DailyTrainDTO dailyTrainDTO) {
        DailyTrain dailyTrain = getByTrainCode(dailyTrainDTO.getCode());
        if (dailyTrain != null) {
            throw new BizException(HttpCodeEnum.TRAIN_CODE_EXIST);
        }
        dailyTrain = BeanUtil.copyProperties(dailyTrainDTO, DailyTrain.class);
        if (dailyTrainDTO.getId() == null) {
            dailyTrain.setId(SnowUtil.getSnowFlakeNextId());
            LocalDateTime now = LocalDateTime.now();
            dailyTrain.setUpdateTime(now);
            dailyTrain.setCreateTime(now);
            save(dailyTrain);
        } else {
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
}

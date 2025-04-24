package com.xiaoqian.business.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaoqian.business.domain.dto.DailyTrainCarriageDTO;
import com.xiaoqian.business.domain.pojo.DailyTrainCarriage;
import com.xiaoqian.business.domain.pojo.TrainCarriage;
import com.xiaoqian.business.domain.query.DailyTrainCarriageQueryDTO;
import com.xiaoqian.business.domain.vo.DailyTrainCarriageVo;
import com.xiaoqian.business.mapper.DailyTrainCarriageMapper;
import com.xiaoqian.business.service.IDailyTrainCarriageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaoqian.business.service.ITrainCarriageService;
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
 * 每日车厢 服务实现类
 * </p>
 *
 * @author xiaoqian
 * @since 2025-04-24
 */
@Service
@RequiredArgsConstructor
public class DailyTrainCarriageServiceImpl extends ServiceImpl<DailyTrainCarriageMapper, DailyTrainCarriage> implements IDailyTrainCarriageService {
    private final ITrainCarriageService trainCarriageService;

    @Override
    public ResponseResult<Void> saveDailyTrainCarriage(DailyTrainCarriageDTO dailyTrainCarriageDTO) {
        DailyTrainCarriage dailyTrainCarriage = BeanUtil.copyProperties(dailyTrainCarriageDTO, DailyTrainCarriage.class);
        if (dailyTrainCarriageDTO.getId() == null) {
            dailyTrainCarriage.setId(SnowUtil.getSnowFlakeNextId());
            LocalDateTime now = LocalDateTime.now();
            dailyTrainCarriage.setUpdateTime(now);
            dailyTrainCarriage.setCreateTime(now);
            save(dailyTrainCarriage);
        } else {
            dailyTrainCarriage.setUpdateTime(LocalDateTime.now());
            updateById(dailyTrainCarriage);
        }

        return ResponseResult.okEmptyResult();
    }

    @Override
    public ResponseResult<PageVo<DailyTrainCarriageVo>> listDailyTrainCarriagePage(DailyTrainCarriageQueryDTO queryDTO) {
        Page<DailyTrainCarriage> page = new Page<>(queryDTO.getPageNum(), queryDTO.getPageSize());
        page(page, new LambdaQueryWrapper<DailyTrainCarriage>()
                .eq(StringUtils.hasText(queryDTO.getCode()), DailyTrainCarriage::getTrainCode, queryDTO.getCode())
                .eq(queryDTO.getDate() != null, DailyTrainCarriage::getDate, queryDTO.getDate())
                .orderByDesc(true, DailyTrainCarriage::getDate)
                .orderByAsc(true, DailyTrainCarriage::getTrainCode)
                .orderByAsc(true, DailyTrainCarriage::getIndexOrder));
        List<DailyTrainCarriage> dailyTrainCarriageList = page.getRecords();
        List<DailyTrainCarriageVo> dailyTrainCarriageVoList = BeanUtil.copyToList(dailyTrainCarriageList, DailyTrainCarriageVo.class);

        return ResponseResult.okResult(new PageVo<>(dailyTrainCarriageVoList, page.getPages(), page.getTotal()));
    }

    @Override
    public ResponseResult<Void> deleteById(Long id) {
        removeById(id);
        return ResponseResult.okEmptyResult();
    }

    /**
     * 生成某日某车次的车厢数据
     */
    public void generateDailyTrainCarriage(String trainCode, LocalDate date) {
        // 删除date天车次的车厢信息
        remove(new LambdaQueryWrapper<DailyTrainCarriage>()
                .eq(DailyTrainCarriage::getTrainCode, trainCode)
                .eq(DailyTrainCarriage::getDate, date));
        // 查询车次的所有车厢
        List<TrainCarriage> trainCarriageList = trainCarriageService.selectByTrainCode(trainCode);
        if (CollectionUtils.isEmpty(trainCarriageList)) {
            log.debug("车次无车厢信息");
            return;
        }
        for (TrainCarriage trainCarriage : trainCarriageList) {
            DailyTrainCarriage dailyTrainCarriage = BeanUtil.copyProperties(trainCarriage, DailyTrainCarriage.class);
            dailyTrainCarriage.setId(SnowUtil.getSnowFlakeNextId());
            dailyTrainCarriage.setDate(date);
            LocalDateTime now = LocalDateTime.now();
            dailyTrainCarriage.setCreateTime(now);
            dailyTrainCarriage.setUpdateTime(now);
            save(dailyTrainCarriage);
        }
    }
}

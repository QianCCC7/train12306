package com.xiaoqian.business.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaoqian.business.domain.dto.StationDTO;
import com.xiaoqian.business.domain.pojo.Station;
import com.xiaoqian.business.domain.query.StationQueryDTO;
import com.xiaoqian.business.domain.vo.StationVo;
import com.xiaoqian.business.mapper.StationMapper;
import com.xiaoqian.business.service.IStationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaoqian.common.domain.ResponseResult;
import com.xiaoqian.common.enums.HttpCodeEnum;
import com.xiaoqian.common.exception.BizException;
import com.xiaoqian.common.query.PageVo;
import com.xiaoqian.common.utils.SnowUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 车站 服务实现类
 * </p>
 *
 * @author xiaoqian
 * @since 2025-04-18
 */
@Service
public class StationServiceImpl extends ServiceImpl<StationMapper, Station> implements IStationService {

    @Override
    public ResponseResult<Void> saveStation(StationDTO stationDTO) {
        Station station = getByName(stationDTO.getName());
        if (station != null) {
            throw new BizException(HttpCodeEnum.STATION_NAME_EXIST);
        }
        station = BeanUtil.copyProperties(stationDTO, Station.class);
        if (stationDTO.getId() == null) {
            station.setId(SnowUtil.getSnowFlakeNextId());
            LocalDateTime now = LocalDateTime.now();
            station.setUpdateTime(now);
            station.setCreateTime(now);
            save(station);
        } else {
            station.setUpdateTime(LocalDateTime.now());
            updateById(station);
        }

        return ResponseResult.okEmptyResult();
    }

    private Station getByName(String name) {
        return lambdaQuery().eq(Station::getName, name).one();
    }

    @Override
    public ResponseResult<PageVo<StationVo>> listStations(StationQueryDTO query) {
        Page<Station> page = new Page<>(query.getPageNum(), query.getPageSize());
        page(page, new LambdaQueryWrapper<Station>()
                .orderByAsc(true, Station::getId));
        List<Station> stationList = page.getRecords();
        List<StationVo> stationVoList = BeanUtil.copyToList(stationList, StationVo.class);

        return ResponseResult.okResult(new PageVo<>(stationVoList, page.getPages(), page.getTotal()));
    }

    @Override
    public ResponseResult<List<StationVo>> getAllStations() {
        List<Station> stationList = lambdaQuery().orderByAsc(Station::getNamePinyin).list();
        List<StationVo> stationVoList = BeanUtil.copyToList(stationList, StationVo.class);

        return ResponseResult.okResult(stationVoList);
    }

    @Override
    public ResponseResult<Void> deleteById(Long id) {
        removeById(id);
        return ResponseResult.okEmptyResult();
    }
}

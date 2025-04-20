package com.xiaoqian.business.controller;


import com.xiaoqian.business.domain.dto.StationDTO;
import com.xiaoqian.business.domain.query.StationQueryDTO;
import com.xiaoqian.business.domain.vo.StationVo;
import com.xiaoqian.business.service.IStationService;
import com.xiaoqian.common.domain.ResponseResult;
import com.xiaoqian.common.query.PageVo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 车站 前端控制器
 * </p>
 *
 * @author xiaoqian
 * @since 2025-04-18
 */
@RestController
@RequestMapping("/admin/station")
@RequiredArgsConstructor
public class StationController {
    private final IStationService stationService;

    @PostMapping("/saveStation")
    public ResponseResult<Void> saveStation(@Valid @RequestBody StationDTO stationDTO) {
        return stationService.saveStation(stationDTO);
    }

    @GetMapping("/listStations")
    public ResponseResult<PageVo<StationVo>> listStations(StationQueryDTO queryDTO) {
        return stationService.listStations(queryDTO);
    }

    @GetMapping("/getAllStations")
    public ResponseResult<List<StationVo>> getAllStations() {
        return stationService.getAllStations();
    }

    @DeleteMapping("/deleteById/{id}")
    public ResponseResult<Void> deleteById(@PathVariable("id") Long id) {
        return stationService.deleteById(id);
    }
}

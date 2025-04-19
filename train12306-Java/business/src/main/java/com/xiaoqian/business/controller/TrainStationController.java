package com.xiaoqian.business.controller;


import com.xiaoqian.business.domain.dto.TrainStationDTO;
import com.xiaoqian.business.domain.query.TrainStationQueryDTO;
import com.xiaoqian.business.domain.vo.TrainStationVo;
import com.xiaoqian.business.service.ITrainStationService;
import com.xiaoqian.common.domain.ResponseResult;
import com.xiaoqian.common.query.PageVo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 车次历经车站 前端控制器
 * </p>
 *
 * @author xiaoqian
 * @since 2025-04-19
 */
@RestController
@RequestMapping("/admin/train-station")
@RequiredArgsConstructor
public class TrainStationController {
    private final ITrainStationService trainStationService;

    @PostMapping("/saveTrainStation")
    public ResponseResult<Void> saveTrainStation(@Valid @RequestBody TrainStationDTO trainStationDTO) {
        return trainStationService.saveTrainStation(trainStationDTO);
    }

    @GetMapping("/listTrainStations")
    public ResponseResult<PageVo<TrainStationVo>> listTrainStations(TrainStationQueryDTO queryDTO) {
        return trainStationService.listTrainStations(queryDTO);
    }

    @DeleteMapping("/deleteById/{id}")
    public ResponseResult<Void> deleteById(@PathVariable("id") Long id) {
        return trainStationService.deleteById(id);
    }
}

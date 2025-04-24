package com.xiaoqian.business.controller;


import com.xiaoqian.business.domain.dto.TrainDTO;
import com.xiaoqian.business.domain.query.TrainQueryDTO;
import com.xiaoqian.business.domain.vo.TrainVo;
import com.xiaoqian.business.service.ITrainService;
import com.xiaoqian.common.domain.ResponseResult;
import com.xiaoqian.common.query.PageVo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 车次 前端控制器
 * </p>
 *
 * @author xiaoqian
 * @since 2025-04-19
 */
@RestController
@RequestMapping("/admin/train")
@RequiredArgsConstructor
public class TrainController {
    private final ITrainService  trainService;

    @PostMapping("/saveTrain")
    public ResponseResult<Void> saveTrain(@Valid @RequestBody TrainDTO trainDTO) {
        return trainService.saveTrain(trainDTO);
    }

    @GetMapping("/listTrains")
    public ResponseResult<PageVo<TrainVo>> listTrains(TrainQueryDTO queryDTO) {
        return trainService.listTrains(queryDTO);
    }

    @GetMapping("/getAllTrains")
    public ResponseResult<List<TrainVo>> getAllTrains() {
        return trainService.getAllTrains();
    }

    @DeleteMapping("/deleteById/{id}")
    public ResponseResult<Void> deleteById(@PathVariable("id") Long id) {
        return trainService.deleteById(id);
    }

    @GetMapping("/generateTrainSeats/{trainCode}")
    public ResponseResult<Void> generateTrainSeats(@PathVariable("trainCode") String trainCode) {
        return trainService.generateTrainSeats(trainCode);
    }
}

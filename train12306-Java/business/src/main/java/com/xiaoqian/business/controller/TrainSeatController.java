package com.xiaoqian.business.controller;


import com.xiaoqian.business.domain.dto.TrainSeatDTO;
import com.xiaoqian.business.domain.query.TrainSeatQueryDTO;
import com.xiaoqian.business.domain.vo.TrainSeatVo;
import com.xiaoqian.business.service.ITrainSeatService;
import com.xiaoqian.common.domain.ResponseResult;
import com.xiaoqian.common.query.PageVo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 座位 前端控制器
 * </p>
 *
 * @author xiaoqian
 * @since 2025-04-20
 */
@RestController
@RequestMapping("/admin/train-seat")
@RequiredArgsConstructor
public class TrainSeatController {
    private final ITrainSeatService trainSeatService;

    @PostMapping("/saveTrainSeat")
    public ResponseResult<Void> saveTrainSeat(@Valid @RequestBody TrainSeatDTO trainSeatDTO) {
        return trainSeatService.saveTrainSeat(trainSeatDTO);
    }

    @GetMapping("/listTrainSeats")
    public ResponseResult<PageVo<TrainSeatVo>> listTrainSeats(TrainSeatQueryDTO queryDTO) {
        return trainSeatService.listTrainSeats(queryDTO);
    }

    @DeleteMapping("/deleteById/{id}")
    public ResponseResult<Void> deleteById(@PathVariable("id") Long id) {
        return trainSeatService.deleteById(id);
    }
}

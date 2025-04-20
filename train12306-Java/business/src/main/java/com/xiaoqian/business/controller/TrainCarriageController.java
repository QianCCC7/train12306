package com.xiaoqian.business.controller;


import com.xiaoqian.business.domain.dto.TrainCarriageDTO;
import com.xiaoqian.business.domain.query.TrainCarriageQueryDTO;
import com.xiaoqian.business.domain.vo.TrainCarriageVo;
import com.xiaoqian.business.service.ITrainCarriageService;
import com.xiaoqian.common.domain.ResponseResult;
import com.xiaoqian.common.query.PageVo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 火车车厢 前端控制器
 * </p>
 *
 * @author xiaoqian
 * @since 2025-04-20
 */
@RestController
@RequestMapping("/admin/train-carriage")
@RequiredArgsConstructor
public class TrainCarriageController {
    private final ITrainCarriageService trainCarriageService;

    @PostMapping("/saveTrainCarriage")
    public ResponseResult<Void> saveTrainCarriage(@Valid @RequestBody TrainCarriageDTO trainCarriageDTO) {
        return trainCarriageService.saveTrainCarriage(trainCarriageDTO);
    }

    @GetMapping("/listTrainCarriages")
    public ResponseResult<PageVo<TrainCarriageVo>> listTrainCarriages(TrainCarriageQueryDTO queryDTO) {
        return trainCarriageService.listTrainCarriages(queryDTO);
    }

    @DeleteMapping("/deleteById/{id}")
    public ResponseResult<Void> deleteById(@PathVariable("id") Long id) {
        return trainCarriageService.deleteById(id);
    }
}

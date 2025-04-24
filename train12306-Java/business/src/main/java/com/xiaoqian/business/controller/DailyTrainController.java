package com.xiaoqian.business.controller;


import com.xiaoqian.business.domain.dto.DailyTrainDTO;
import com.xiaoqian.business.domain.query.DailyTrainQueryDTO;
import com.xiaoqian.business.domain.vo.DailyTrainVo;
import com.xiaoqian.business.service.IDailyTrainService;
import com.xiaoqian.common.domain.ResponseResult;
import com.xiaoqian.common.query.PageVo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;


/**
 * <p>
 * 每日车次 前端控制器
 * </p>
 *
 * @author xiaoqian
 * @since 2025-04-24
 */
@RestController
@RequestMapping("/admin/daily-train")
@RequiredArgsConstructor
public class DailyTrainController {
    private final IDailyTrainService dailyTrainService;

    @PostMapping("/saveDailyTrain")
    public ResponseResult<Void> saveDailyTrain(@Valid @RequestBody DailyTrainDTO dailyTrainDTO) {
        return dailyTrainService.saveDailyTrain(dailyTrainDTO);
    }

    @GetMapping("/listDailyTrainPage")
    public ResponseResult<PageVo<DailyTrainVo>> listDailyTrainPage(DailyTrainQueryDTO queryDTO) {
        return dailyTrainService.listDailyTrainPage(queryDTO);
    }

    @DeleteMapping("/deleteById/{id}")
    public ResponseResult<Void> deleteById(@PathVariable("id") Long id) {
        return dailyTrainService.deleteById(id);
    }

    @GetMapping("/generateDailyTrain/{date}")
    public ResponseResult<Void> generateDailyTrain(@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        return dailyTrainService.generateDailyTrain(date);
    }
}

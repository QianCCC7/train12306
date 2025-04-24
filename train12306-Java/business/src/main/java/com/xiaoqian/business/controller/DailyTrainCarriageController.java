package com.xiaoqian.business.controller;


import com.xiaoqian.business.domain.dto.DailyTrainCarriageDTO;
import com.xiaoqian.business.domain.query.DailyTrainCarriageQueryDTO;
import com.xiaoqian.business.domain.vo.DailyTrainCarriageVo;
import com.xiaoqian.business.service.IDailyTrainCarriageService;
import com.xiaoqian.common.domain.ResponseResult;
import com.xiaoqian.common.query.PageVo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 每日车厢 前端控制器
 * </p>
 *
 * @author xiaoqian
 * @since 2025-04-24
 */
@RestController
@RequestMapping("/admin/daily-train-carriage")
@RequiredArgsConstructor
public class DailyTrainCarriageController {
    private final IDailyTrainCarriageService dailyTrainCarriageService;

    @PostMapping("/saveDailyTrainCarriage")
    public ResponseResult<Void> saveDailyTrainCarriage(@Valid @RequestBody DailyTrainCarriageDTO dailyTrainCarriageDTO) {
        return dailyTrainCarriageService.saveDailyTrainCarriage(dailyTrainCarriageDTO);
    }

    @GetMapping("/listDailyTrainCarriagePage")
    public ResponseResult<PageVo<DailyTrainCarriageVo>> listDailyTrainCarriagePage(DailyTrainCarriageQueryDTO queryDTO) {
        return dailyTrainCarriageService.listDailyTrainCarriagePage(queryDTO);
    }

    @DeleteMapping("/deleteById/{id}")
    public ResponseResult<Void> deleteById(@PathVariable("id") Long id) {
        return dailyTrainCarriageService.deleteById(id);
    }
}

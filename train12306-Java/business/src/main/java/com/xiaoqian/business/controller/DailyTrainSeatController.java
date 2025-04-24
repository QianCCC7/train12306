package com.xiaoqian.business.controller;


import com.xiaoqian.business.domain.dto.DailyTrainSeatDTO;
import com.xiaoqian.business.domain.query.DailyTrainSeatQueryDTO;
import com.xiaoqian.business.domain.vo.DailyTrainSeatVo;
import com.xiaoqian.business.service.IDailyTrainSeatService;
import com.xiaoqian.common.domain.ResponseResult;
import com.xiaoqian.common.query.PageVo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 每日座位 前端控制器
 * </p>
 *
 * @author xiaoqian
 * @since 2025-04-24
 */
@RestController
@RequestMapping("/admin/daily-train-seat")
@RequiredArgsConstructor
public class DailyTrainSeatController {
    private final IDailyTrainSeatService dailyTrainSeatService;

    @PostMapping("/saveDailyTrainSeat")
    public ResponseResult<Void> saveDailyTrainSeat(@Valid @RequestBody DailyTrainSeatDTO dailyTrainSeatDTO) {
        return dailyTrainSeatService.saveDailyTrainCarriage(dailyTrainSeatDTO);
    }

    @GetMapping("/listDailyTrainSeatPage")
    public ResponseResult<PageVo<DailyTrainSeatVo>> listDailyTrainSeatPage(DailyTrainSeatQueryDTO queryDTO) {
        return dailyTrainSeatService.listDailyTrainSeatPage(queryDTO);
    }

    @DeleteMapping("/deleteById/{id}")
    public ResponseResult<Void> deleteById(@PathVariable("id") Long id) {
        return dailyTrainSeatService.deleteById(id);
    }
}

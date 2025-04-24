package com.xiaoqian.business.controller;


import com.xiaoqian.business.domain.dto.DailyTrainStationDTO;
import com.xiaoqian.business.domain.query.DailyTrainStationQueryDTO;
import com.xiaoqian.business.domain.vo.DailyTrainStationVo;
import com.xiaoqian.business.service.IDailyTrainStationService;
import com.xiaoqian.common.domain.ResponseResult;
import com.xiaoqian.common.query.PageVo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 每日历经车站 前端控制器
 * </p>
 *
 * @author xiaoqian
 * @since 2025-04-24
 */
@RestController
@RequestMapping("/admin/daily-train-station")
@RequiredArgsConstructor
public class DailyTrainStationController {
    private final IDailyTrainStationService dailyTrainStationService;

    @PostMapping("/saveDailyTrainStation")
    public ResponseResult<Void> saveDailyTrainStation(@Valid @RequestBody DailyTrainStationDTO dailyTrainStationDTO) {
        return dailyTrainStationService.saveDailyTrainStation(dailyTrainStationDTO);
    }

    @GetMapping("/listDailyTrainStationPage")
    public ResponseResult<PageVo<DailyTrainStationVo>> listDailyTrainStationPage(DailyTrainStationQueryDTO queryDTO) {
        return dailyTrainStationService.listDailyTrainStationPage(queryDTO);
    }

    @DeleteMapping("/deleteById/{id}")
    public ResponseResult<Void> deleteById(@PathVariable("id") Long id) {
        return dailyTrainStationService.deleteById(id);
    }
}

package com.xiaoqian.business.controller.member;


import com.xiaoqian.business.domain.query.DailyTrainStationQueryDTO;
import com.xiaoqian.business.domain.vo.DailyTrainStationVo;
import com.xiaoqian.business.service.IDailyTrainStationService;
import com.xiaoqian.common.domain.ResponseResult;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 每日历经车站 前端控制器
 * </p>
 *
 * @author xiaoqian
 * @since 2025-04-24
 */
@RestController
@RequestMapping("/daily-train-station")
@RequiredArgsConstructor
public class DailyTrainStationMemberController {
    private final IDailyTrainStationService dailyTrainStationService;
    @GetMapping("/getByTrainCodeAndDate")
    public ResponseResult<List<DailyTrainStationVo>> getByTrainCodeAndDate(@Valid DailyTrainStationQueryDTO queryDTO) {
        return dailyTrainStationService.getByTrainCodeAndDate(queryDTO);
    }
}

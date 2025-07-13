package com.xiaoqian.business.controller.member;


import com.xiaoqian.business.domain.query.DailyTrainSeatQueryDTO;
import com.xiaoqian.business.domain.vo.DailyTrainSeatVo;
import com.xiaoqian.business.service.IDailyTrainSeatService;
import com.xiaoqian.common.domain.ResponseResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 每日座位 前端控制器
 * </p>
 *
 * @author xiaoqian
 * @since 2025-07-14
 */
@RestController
@RequestMapping("/daily-train-seat")
@RequiredArgsConstructor
public class DailyTrainSeatMemberController {
    private final IDailyTrainSeatService dailyTrainSeatService;

    @GetMapping("/getSellSeatList")
    public ResponseResult<List<DailyTrainSeatVo>> getSellSeatList(DailyTrainSeatQueryDTO dailyTrainSeatQueryDTO) {
        return dailyTrainSeatService.getSellSeatList(dailyTrainSeatQueryDTO);
    }
}

package com.xiaoqian.business.controller.member;


import com.xiaoqian.business.domain.vo.StationVo;
import com.xiaoqian.business.service.IStationService;
import com.xiaoqian.common.domain.ResponseResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 车站 前端控制器
 * </p>
 *
 * @author xiaoqian
 * @since 2025-04-26
 */
@RestController
@RequestMapping("/station")
@RequiredArgsConstructor
public class StationMemberController {
    private final IStationService stationService;

    @GetMapping("/getAllStations")
    public ResponseResult<List<StationVo>> getAllStations() {
        return stationService.getAllStations();
    }

}

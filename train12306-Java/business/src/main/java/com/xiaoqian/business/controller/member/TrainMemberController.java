package com.xiaoqian.business.controller.member;


import com.xiaoqian.business.domain.vo.TrainVo;
import com.xiaoqian.business.service.ITrainService;
import com.xiaoqian.common.domain.ResponseResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 车次 前端控制器
 * </p>
 *
 * @author xiaoqian
 * @since 2025-04-26
 */
@RestController
@RequestMapping("/train")
@RequiredArgsConstructor
public class TrainMemberController {
    private final ITrainService  trainService;

    @GetMapping("/getAllTrains")
    public ResponseResult<List<TrainVo>> getAllTrains() {
        return trainService.getAllTrains();
    }

}

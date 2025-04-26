package com.xiaoqian.business.controller.member;

import com.xiaoqian.business.domain.query.DailyTrainTicketQueryDTO;
import com.xiaoqian.business.domain.vo.DailyTrainTicketVo;
import com.xiaoqian.business.service.IDailyTrainTicketService;
import com.xiaoqian.common.domain.ResponseResult;
import com.xiaoqian.common.query.PageVo;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 余票信息 前端控制器
 * </p>
 *
 * @author xiaoqian
 * @since 2025-04-26
 */
@RestController
@RequestMapping("/daily-train-ticket")
@RequiredArgsConstructor
public class DailyTrainTicketMemberController {
    private final IDailyTrainTicketService dailyTrainTicketService;

    @GetMapping("/listDailyTrainTicketPage")
    public ResponseResult<PageVo<DailyTrainTicketVo>> listDailyTrainTicketPage(DailyTrainTicketQueryDTO queryDTO) {
        return dailyTrainTicketService.listDailyTrainTicketPage(queryDTO);
    }
}

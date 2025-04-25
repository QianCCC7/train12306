package com.xiaoqian.business.controller;


import com.xiaoqian.business.domain.dto.DailyTrainTicketDTO;
import com.xiaoqian.business.domain.query.DailyTrainTicketQueryDTO;
import com.xiaoqian.business.domain.vo.DailyTrainTicketVo;
import com.xiaoqian.business.service.IDailyTrainTicketService;
import com.xiaoqian.common.domain.ResponseResult;
import com.xiaoqian.common.query.PageVo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 余票信息 前端控制器
 * </p>
 *
 * @author xiaoqian
 * @since 2025-04-25
 */
@RestController
@RequestMapping("/admin/daily-train-ticket")
@RequiredArgsConstructor
public class DailyTrainTicketController {
    private final IDailyTrainTicketService dailyTrainTicketService;

    @PostMapping("/saveDailyTrainTicket")
    public ResponseResult<Void> saveDailyTrainTicket(@Valid @RequestBody DailyTrainTicketDTO dailyTrainTicketDTO) {
        return dailyTrainTicketService.saveDailyTrainTicket(dailyTrainTicketDTO);
    }

    @GetMapping("/listDailyTrainTicketPage")
    public ResponseResult<PageVo<DailyTrainTicketVo>> listDailyTrainTicketPage(DailyTrainTicketQueryDTO queryDTO) {
        return dailyTrainTicketService.listDailyTrainTicketPage(queryDTO);
    }

    @DeleteMapping("/deleteById/{id}")
    public ResponseResult<Void> deleteById(@PathVariable("id") Long id) {
        return dailyTrainTicketService.deleteById(id);
    }
}

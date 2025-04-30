package com.xiaoqian.business.controller;


import com.xiaoqian.business.domain.dto.ConfirmOrderDTO;
import com.xiaoqian.business.domain.query.ConfirmOrderQueryDTO;
import com.xiaoqian.business.domain.vo.ConfirmOrderVo;
import com.xiaoqian.business.service.IConfirmOrderService;
import com.xiaoqian.common.domain.ResponseResult;
import com.xiaoqian.common.query.PageVo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 确认订单 前端控制器
 * </p>
 *
 * @author xiaoqian
 * @since 2025-04-30
 */
@RestController
@RequestMapping("/admin/confirm-order")
@RequiredArgsConstructor
public class ConfirmOrderController {
    private final IConfirmOrderService confirmOrderService;

    @PostMapping("/saveOrder")
    public ResponseResult<Void> saveOrder(@RequestBody @Valid ConfirmOrderDTO confirmOrderDTO) {
        return confirmOrderService.saveOrder(confirmOrderDTO);
    }

    @GetMapping("/listOrderPage")
    public ResponseResult<PageVo<ConfirmOrderVo>> listOrderPage(ConfirmOrderQueryDTO confirmOrderQueryDTO) {
        return confirmOrderService.listOrderPage(confirmOrderQueryDTO);
    }
}

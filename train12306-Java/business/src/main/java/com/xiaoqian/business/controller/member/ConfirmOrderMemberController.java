package com.xiaoqian.business.controller.member;


import com.xiaoqian.business.domain.dto.ConfirmOrderDTO;
import com.xiaoqian.business.service.IConfirmOrderService;
import com.xiaoqian.common.domain.ResponseResult;
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
@RequestMapping("/confirm-order")
@RequiredArgsConstructor
public class ConfirmOrderMemberController {
    private final IConfirmOrderService confirmOrderService;

        @PostMapping("/submitOrder")
    public ResponseResult<Void> submitOrder(@RequestBody @Valid ConfirmOrderDTO confirmOrderDTO) {
        return confirmOrderService.submitOrder(confirmOrderDTO);
    }
}

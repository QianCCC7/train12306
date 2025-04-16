package com.xiaoqian.member.controller;


import com.xiaoqian.common.domain.ResponseResult;
import com.xiaoqian.member.domain.dto.PassengerDTO;
import com.xiaoqian.member.domain.pojo.Passenger;
import com.xiaoqian.member.service.IPassengerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 乘车人 前端控制器
 * </p>
 *
 * @author xiaoqian
 * @since 2025-04-16
 */
@RestController
@RequestMapping("/passenger")
@RequiredArgsConstructor
public class PassengerController {
    private final IPassengerService passengerService;

    @PostMapping("/savePassenger")
    public ResponseResult<Void> savePassenger(@Valid @RequestBody PassengerDTO passengerDTO) {
        return passengerService.savePassenger(passengerDTO);
    }

    @GetMapping("/get")
    public ResponseResult<Passenger> getPassenger() {
        return ResponseResult.okResult(passengerService.lambdaQuery().eq(Passenger::getMemberId, 1).one());
    }
}

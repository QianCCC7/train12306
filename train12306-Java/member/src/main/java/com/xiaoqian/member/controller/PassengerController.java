package com.xiaoqian.member.controller;


import com.xiaoqian.common.context.MemberContext;
import com.xiaoqian.common.domain.ResponseResult;
import com.xiaoqian.common.query.PageVo;
import com.xiaoqian.member.domain.dto.PassengerDTO;
import com.xiaoqian.member.domain.pojo.Passenger;
import com.xiaoqian.member.domain.query.PassengerQueryDTO;
import com.xiaoqian.member.domain.vo.PassengerVo;
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

    @GetMapping("/listPassengers")
    public ResponseResult<PageVo<PassengerVo>> listPassengers(PassengerQueryDTO query) {
        query.setMemberId(MemberContext.getId());
        return passengerService.listPassengers(query);
    }

    @GetMapping("/get")
    public ResponseResult<Passenger> getPassenger() {
        return ResponseResult.okResult(passengerService.lambdaQuery().eq(Passenger::getMemberId, 1912053495805120512L).one());
    }
}

package com.xiaoqian.business.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PassengerTicketsDTO {
    @NotNull(message = "【乘客ID】不能为空")
    private Long passengerId;

    @NotBlank(message = "【乘客票种】不能为空")
    private String passengerType;

    @NotBlank(message = "【乘客名称】不能为空")
    private String passengerName;

    @NotBlank(message = "【乘客身份证】不能为空")
    private String passengerIdCard;

    @NotBlank(message = "【座位类型】不能为空")
    private String seatType;

    private String seat;
}

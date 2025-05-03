package com.xiaoqian.common.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberTicketDTO {

    @NotNull(message = "【会员id】不能为空")
    private Long memberId;

    @NotNull(message = "【乘客id】不能为空")
    private Long passengerId;

    @NotBlank(message = "【乘客名字】不能为空")
    private String passengerName;

    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @NotNull(message = "【日期】不能为空")
    private LocalDate trainDate;

    @NotBlank(message = "【车次编号】不能为空")
    private String trainCode;

    @NotNull(message = "【箱序】不能为空")
    private Integer carriageIndex;

    @NotBlank(message = "【排号】不能为空")
    private String seatRow;

    @NotBlank(message = "【列号】不能为空")
    private String seatCol;

    @NotBlank(message = "【出发站】不能为空")
    private String startStation;

    @JsonFormat(pattern = "HH:mm:ss",timezone = "GMT+8")
    @NotNull(message = "【出发时间】不能为空")
    private LocalTime startTime;

    @NotBlank(message = "【到达站】不能为空")
    private String endStation;

    @JsonFormat(pattern = "HH:mm:ss",timezone = "GMT+8")
    @NotNull(message = "【到站时间】不能为空")
    private LocalTime endTime;

    @NotBlank(message = "【座位类型】不能为空")
    private String seatType;
}

package com.xiaoqian.member.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.xiaoqian.common.enums.SeatColEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberTicketVo {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    @JsonSerialize(using = ToStringSerializer.class)
    private Long memberId;
    @JsonSerialize(using = ToStringSerializer.class)
    private Long passengerId;

    private String passengerName;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+08")
    private LocalDate trainDate;

    private String trainCode;

    private Integer carriageIndex;

    private String seatRow;

    private SeatColEnum seatCol;

    private String startStation;
    @JsonFormat(pattern = "HH:mm:ss", timezone = "GMT+08")
    private LocalTime startTime;

    private String endStation;
    @JsonFormat(pattern = "HH:mm:ss", timezone = "GMT+08")
    private LocalTime endTime;

    private String seatType;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08")
    private LocalDateTime createTime;


}

package com.xiaoqian.business.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DailyTrainTicketVo {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private LocalDate date;

    private String trainCode;

    private String start;

    private String startPinyin;
    @JsonFormat(pattern = "HH:mm:ss",timezone = "GMT+8")
    private LocalTime startTime;

    private Integer startIndex;

    private String end;

    private String endPinyin;
    @JsonFormat(pattern = "HH:mm:ss",timezone = "GMT+8")
    private LocalTime endTime;

    private Integer endIndex;

    private Integer ydz;

    private BigDecimal ydzPrice;

    private Integer edz;

    private BigDecimal edzPrice;

    private Integer rw;

    private BigDecimal rwPrice;

    private Integer yw;

    private BigDecimal ywPrice;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08")
    private LocalDateTime createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08")
    private LocalDateTime updateTime;
}

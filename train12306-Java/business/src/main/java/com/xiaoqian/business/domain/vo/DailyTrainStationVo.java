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
public class DailyTrainStationVo {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private LocalDate date;
    private String trainCode;

    private Integer indexOrder;

    private String name;

    private String namePinyin;
    @JsonFormat(pattern = "HH:mm:ss", timezone = "GMT+08")
    private LocalTime inTime;
    @JsonFormat(pattern = "HH:mm:ss", timezone = "GMT+08")
    private LocalTime outTime;
    @JsonFormat(pattern = "HH:mm:ss", timezone = "GMT+08")
    private LocalTime stopTime;

    private BigDecimal km;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08")
    private LocalDateTime createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08")
    private LocalDateTime updateTime;

}

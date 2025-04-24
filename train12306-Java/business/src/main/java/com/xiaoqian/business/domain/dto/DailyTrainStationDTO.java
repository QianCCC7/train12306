package com.xiaoqian.business.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DailyTrainStationDTO {

    private Long id;
    @NotNull(message = "日期不能为空")
    private LocalDate date;
    @NotBlank(message = "车次编码不能为空")
    private String trainCode;
    @NotNull(message = "站序不能为空")
    private Integer indexOrder;
    @NotBlank(message = "车站名称不能为空")
    private String name;
    @NotBlank(message = "车站拼音不能为空")
    private String namePinyin;
    @NotNull(message = "进站时间不能为空")
    private LocalTime inTime;
    @NotNull(message = "出站时间不能为空")
    private LocalTime outTime;
    @NotNull(message = "停站时长不能为空")
    private LocalTime stopTime;
    @NotNull(message = "里程不能为空")
    private BigDecimal km;
}

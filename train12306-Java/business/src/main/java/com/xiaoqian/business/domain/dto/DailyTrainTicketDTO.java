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
@NoArgsConstructor
@AllArgsConstructor
public class DailyTrainTicketDTO {
    private Long id;
    @NotNull(message = "日期不能为空")
    private LocalDate date;
    @NotBlank(message = "车次编码不能为空")
    private String trainCode;
    @NotBlank(message = "始发站不能为空")
    private String start;
    @NotBlank(message = "始发站拼音不能为空")
    private String startPinyin;
    @NotNull(message = "出发时间不能为空")
    private LocalTime startTime;
    @NotNull(message = "出发站序不能为空")
    private Integer startIndex;
    @NotBlank(message = "终点站不能为空")
    private String end;
    @NotBlank(message = "终点站拼音不能为空")
    private String endPinyin;
    @NotNull(message = "到站时间不能为空")
    private LocalTime endTime;
    @NotNull(message = "终点站序不能为空")
    private Integer endIndex;
    @NotNull(message = "一等座余票不能为空")
    private Integer ydz;
    @NotNull(message = "一等座票价不能为空")
    private BigDecimal ydzPrice;
    @NotNull(message = "二等座余票不能为空")
    private Integer edz;
    @NotNull(message = "二等座票价不能为空")
    private BigDecimal edzPrice;
    @NotNull(message = "软卧余票不能为空")
    private Integer rw;
    @NotNull(message = "软卧票价不能为空")
    private BigDecimal rwPrice;
    @NotNull(message = "硬卧余票不能为空")
    private Integer yw;
    @NotNull(message = "硬卧票价不能为空")
    private BigDecimal ywPrice;
}

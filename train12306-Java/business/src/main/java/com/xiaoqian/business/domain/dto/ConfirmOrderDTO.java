package com.xiaoqian.business.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConfirmOrderDTO {

    private Long id;
    @NotNull(message = "日期不能为空")
    private LocalDate date;
    @NotBlank(message = "车次编码不能为空")
    private String trainCode;
    @NotBlank(message = "车次始发站不能为空")
    private String start;
    @NotBlank(message = "车次终点站不能为空")
    private String end;
    @NotNull(message = "每日余票id不能为空")
    private Long dailyTrainTicketId;
    @NotNull(message = "总票价不能为空")
    private BigDecimal totalPrice;
    @NotNull(message = "车票与乘车人信息不能为空")
    private List<PassengerTicketsDTO> tickets;

}

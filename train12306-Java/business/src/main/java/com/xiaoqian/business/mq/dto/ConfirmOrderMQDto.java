package com.xiaoqian.business.mq.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConfirmOrderMQDto {
    private LocalDate date;
    private String trainCode;
}

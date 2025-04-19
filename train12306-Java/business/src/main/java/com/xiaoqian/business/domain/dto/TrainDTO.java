package com.xiaoqian.business.domain.dto;

import com.xiaoqian.business.enums.TrainTypeEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrainDTO {
    private Long id;
    @NotBlank(message = "车次编码不能为空")
    private String code;
    @NotNull(message = "车次类型不能为空")
    private TrainTypeEnum type;
    @NotBlank(message = "始发站不能为空")
    private String start;
    @NotBlank(message = "始发站拼音不能为空")
    private String startPinyin;
    @NotNull(message = "出发时间不能为空")
    private LocalTime startTime;
    @NotBlank(message = "终点站不能为空")
    private String end;
    @NotBlank(message = "终点站拼音不能为空")
    private String endPinyin;
    @NotNull(message = "到站时间不能为空")
    private LocalTime endTime;
}

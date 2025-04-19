package com.xiaoqian.business.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.xiaoqian.business.enums.TrainTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrainVo {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    private String code;

    private TrainTypeEnum type;

    private String start;

    private String startPinyin;
    @JsonFormat(pattern = "HH:mm:ss", timezone = "GMT+08")
    private LocalTime startTime;

    private String end;

    private String endPinyin;
    @JsonFormat(pattern = "HH:mm:ss", timezone = "GMT+08")
    private LocalTime endTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08")
    private LocalDateTime createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08")
    private LocalDateTime updateTime;
}

package com.xiaoqian.business.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrainStationVo implements Serializable {

    private Long id;

    private String trainCode;

    private Integer index;

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

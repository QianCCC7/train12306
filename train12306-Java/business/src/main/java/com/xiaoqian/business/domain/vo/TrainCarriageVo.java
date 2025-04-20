package com.xiaoqian.business.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.xiaoqian.business.enums.SeatTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * <p>
 * 火车车厢
 * </p>
 *
 * @author xiaoqian
 * @since 2025-04-20
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrainCarriageVo {

    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    private String trainCode;

    private Integer index;

    private SeatTypeEnum seatType;

    private Integer seatCount;

    private Integer rowCount;

    private Integer colCount;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08")
    private LocalDateTime createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08")
    private LocalDateTime updateTime;


}

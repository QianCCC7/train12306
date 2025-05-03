package com.xiaoqian.business.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.xiaoqian.common.enums.SeatColEnum;
import com.xiaoqian.business.enums.SeatTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 * 座位
 * </p>
 *
 * @author xiaoqian
 * @since 2025-04-20
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DailyTrainSeatVo {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private LocalDate date;
    private String trainCode;

    private Integer carriageIndex;

    private String rowOrder;

    private SeatColEnum col;

    private SeatTypeEnum seatType;

    private Integer carriageSeatIndex;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08")
    private LocalDateTime createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08")
    private LocalDateTime updateTime;
    private String sell;

}

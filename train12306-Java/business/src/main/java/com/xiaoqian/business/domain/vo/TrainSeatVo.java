package com.xiaoqian.business.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.xiaoqian.common.enums.SeatColEnum;
import com.xiaoqian.common.enums.SeatTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
public class TrainSeatVo {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

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


}

package com.xiaoqian.business.domain.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.xiaoqian.business.enums.SeatColEnum;
import com.xiaoqian.business.enums.SeatTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
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

    private String row;

    private SeatColEnum col;

    private SeatTypeEnum seatType;

    private Integer carriageSeatIndex;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08")
    private LocalDateTime createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08")
    private LocalDateTime updateTime;


}

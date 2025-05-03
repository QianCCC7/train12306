package com.xiaoqian.business.domain.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import com.xiaoqian.common.enums.SeatColEnum;
import com.xiaoqian.common.enums.SeatTypeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 每日座位
 * </p>
 *
 * @author xiaoqian
 * @since 2025-04-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("daily_train_seat")
public class DailyTrainSeat implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.NONE)
    private Long id;

    /**
     * 日期
     */
    private LocalDate date;

    /**
     * 车次编号
     */
    private String trainCode;

    /**
     * 箱序
     */
    private Integer carriageIndex;

    /**
     * 排号|01, 02
     */
    private String rowOrder;

    /**
     * 列号|枚举[SeatColEnum]
     */
    private SeatColEnum col;

    /**
     * 座位类型|枚举[SeatTypeEnum]
     */
    private SeatTypeEnum seatType;

    /**
     * 同车箱座序
     */
    private Integer carriageSeatIndex;

    /**
     * 售卖情况|将经过的车站用01拼接，0表示可卖，1表示已卖
     */
    private String sell;

    /**
     * 新增时间
     */
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;


}

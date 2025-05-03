package com.xiaoqian.member.domain.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDate;
import java.time.LocalTime;
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
 * 乘客购买的车票记录
 * </p>
 *
 * @author xiaoqian
 * @since 2025-05-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("member_ticket")
public class MemberTicket implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.NONE)
    private Long id;

    /**
     * 会员id
     */
    private Long memberId;

    /**
     * 乘客id
     */
    private Long passengerId;

    /**
     * 乘客姓名
     */
    private String passengerName;

    /**
     * 日期
     */
    private LocalDate trainDate;

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
    private String seatRow;

    /**
     * 列号|枚举[SeatColEnum]
     */
    private SeatColEnum seatCol;

    /**
     * 出发站
     */
    private String startStation;

    /**
     * 出发时间
     */
    private LocalTime startTime;

    /**
     * 到达站
     */
    private String endStation;

    /**
     * 到站时间
     */
    private LocalTime endTime;

    /**
     * 座位类型|枚举[SeatTypeEnum]
     */
    private SeatTypeEnum seatType;

    /**
     * 新增时间
     */
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;


}

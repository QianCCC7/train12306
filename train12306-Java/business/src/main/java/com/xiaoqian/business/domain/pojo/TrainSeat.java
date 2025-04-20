package com.xiaoqian.business.domain.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import com.xiaoqian.business.enums.SeatColEnum;
import com.xiaoqian.business.enums.SeatTypeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 座位
 * </p>
 *
 * @author xiaoqian
 * @since 2025-04-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("train_seat")
public class TrainSeat implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.NONE)
    private Long id;

    /**
     * 车次编号
     */
    private String trainCode;

    /**
     * 厢序
     */
    private Integer carriageIndex;

    /**
     * 排号|01, 02
     */
    private String row;

    /**
     * 列号|枚举[SeatColEnum]
     */
    private SeatColEnum col;

    /**
     * 座位类型|枚举[SeatTypeEnum]
     */
    private SeatTypeEnum seatType;

    /**
     * 同车厢座序
     */
    private Integer carriageSeatIndex;

    /**
     * 新增时间
     */
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;


}

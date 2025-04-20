package com.xiaoqian.business.domain.dto;

import com.xiaoqian.business.enums.SeatColEnum;
import com.xiaoqian.business.enums.SeatTypeEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


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
public class TrainSeatDTO {

    private Long id;
    @NotBlank(message = "车次编码不能为空")
    private String trainCode;
    @NotNull(message = "厢号不能为空")
    private Integer carriageIndex;
    @NotBlank(message = "排号不能为空")
    private String row;
    @NotNull(message = "列号不能为空")
    private SeatColEnum col;
    @NotNull(message = "座位类型不能为空")
    private SeatTypeEnum seatType;
    @NotNull(message = "同车厢坐序不能为空")
    private Integer carriageSeatIndex;


}

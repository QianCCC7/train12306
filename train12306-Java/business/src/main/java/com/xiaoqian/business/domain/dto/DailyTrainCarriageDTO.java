package com.xiaoqian.business.domain.dto;

import com.xiaoqian.business.enums.SeatTypeEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DailyTrainCarriageDTO {

    private Long id;
    @NotNull(message = "日期不能为空")
    private LocalDate date;
    @NotBlank(message = "车次编码不能为空")
    private String trainCode;
    @NotNull(message = "厢号不能为空")
    private Integer indexOrder;
    @NotNull(message = "车座类型不能为空")
    private SeatTypeEnum seatType;
    @NotNull(message = "座位数不能为空")
    private Integer seatCount;
    @NotNull(message = "座位排数不能为空")
    private Integer rowCount;
    @NotNull(message = "座位列数不能为空")
    private Integer colCount;

}

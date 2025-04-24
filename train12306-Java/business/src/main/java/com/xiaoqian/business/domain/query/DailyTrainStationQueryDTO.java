package com.xiaoqian.business.domain.query;

import com.xiaoqian.common.query.PageQuery;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class DailyTrainStationQueryDTO extends PageQuery {
    private String code;
    private LocalDate date;
}

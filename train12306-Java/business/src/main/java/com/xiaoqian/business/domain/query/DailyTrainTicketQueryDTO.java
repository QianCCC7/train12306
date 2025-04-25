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
public class DailyTrainTicketQueryDTO extends PageQuery {
    private LocalDate date;
    private String code;
    private String start;
    private String end;
}

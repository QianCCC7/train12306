package com.xiaoqian.member.domain.query;

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
public class MemberTicketQueryDTO extends PageQuery {
    private String trainCode;
    private LocalDate date;
}

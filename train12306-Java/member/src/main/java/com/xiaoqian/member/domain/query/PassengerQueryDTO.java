package com.xiaoqian.member.domain.query;

import com.xiaoqian.common.query.PageQuery;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class PassengerQueryDTO extends PageQuery {
    private Long memberId;
}

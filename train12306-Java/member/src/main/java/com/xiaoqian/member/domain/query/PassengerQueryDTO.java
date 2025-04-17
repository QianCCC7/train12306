package com.xiaoqian.member.domain.query;

import com.xiaoqian.common.query.PageQuery;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PassengerQueryDTO extends PageQuery {
    private Long memberId;
}

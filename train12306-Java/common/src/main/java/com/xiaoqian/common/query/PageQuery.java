package com.xiaoqian.common.query;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageQuery {
    public static final Integer DEFAULT_PAGE_NUM = 1;
    public static final Integer DEFAULT_PAGE_SIZE = 10;

    private Integer pageNum = DEFAULT_PAGE_NUM;

    private Integer pageSize = DEFAULT_PAGE_SIZE;
}

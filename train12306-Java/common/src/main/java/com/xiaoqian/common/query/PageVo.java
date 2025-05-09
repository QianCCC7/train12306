package com.xiaoqian.common.query;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageVo<T> {
    private List<T> rows;

    private Long totalPages;

    private Long totalRecords;
}

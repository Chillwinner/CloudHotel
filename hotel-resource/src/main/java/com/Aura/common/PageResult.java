package com.Aura.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

@Data
@AllArgsConstructor
public class PageResult<T> {
    private Long total;     // 总条数
    private List<T> list;   // 当前页数据
}
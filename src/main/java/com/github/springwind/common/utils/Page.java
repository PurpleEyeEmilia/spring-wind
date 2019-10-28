package com.github.springwind.common.utils;

import lombok.Data;

import java.util.Collection;

/**
 * @author pengnian
 * @version V1.0
 * @date 2019/10/11 10:10
 * @Desc
 */
@Data
public class Page<T> {

    private Integer pageNo;

    private Integer pageSize;

    private Long total;

    private Collection<T> dataList;

    public Page() {
    }

    public Page(Integer pageNo, Integer pageSize, Collection<T> dataList) {
        this.pageNo = pageNo;
        this.pageSize = pageSize;
        this.dataList = dataList;
    }

    public Page(Integer pageNo, Integer pageSize, Long total, Collection<T> dataList) {
        this.pageNo = pageNo;
        this.pageSize = pageSize;
        this.total = total;
        this.dataList = dataList;
    }
}

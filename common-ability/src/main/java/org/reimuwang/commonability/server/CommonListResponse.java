package org.reimuwang.commonability.server;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * server返回list结果的通用结构体
 * @param <T> 返回结果list中的元素类型
 */
@Data
public class CommonListResponse<T> {

    /**
     * 当前请求返回的结果数据
     */
    private List<T> dataList = new ArrayList<>();

    /**
     * 数据总数，通常用来做分页判定
     */
    private Integer totalCount = 0;

    public CommonListResponse() {
    }

    public CommonListResponse(List<T> dataList, Integer totalCount) {
        this.dataList = dataList;
        this.totalCount = totalCount;
    }
}

package org.reimuwang.commonability.http;

import lombok.Data;

@Data
public class CommonRequest {

    /**
     * 分页页码，自1开始
     */
    protected Integer page = 1;

    /**
     * 每页个数
     */
    protected Integer pageSize = 10;
}

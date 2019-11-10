package org.reimuwang.commonability.http;

import lombok.extern.slf4j.Slf4j;

/**
 * http请求工具类，基于builder构造
 */
@Slf4j
public class HttpClientUtil {

    public String test() {
        log.info("HttpClientUtil.test被调用");
        return "成功";
    }
}

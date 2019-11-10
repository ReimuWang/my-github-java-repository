package org.reimuwang.filemanagement.test;

import lombok.extern.slf4j.Slf4j;
import org.reimuwang.commonability.http.HttpClientUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class TestController {

    @RequestMapping("/test")
    public String testWeb() {
        log.info("请求了一次testWeb接口");
        String httpResult = new HttpClientUtil().test();
        log.info("请求HttpClientUtil，返回值=" + httpResult);
        return httpResult;
    }
}

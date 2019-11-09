package org.reimuwang.filemanagement.test;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    /**
     * 请求地址形如: http://localhost:8080/test
     * @return
     */
    @RequestMapping("/test")
    public String test() {
        return "test";
    }
}

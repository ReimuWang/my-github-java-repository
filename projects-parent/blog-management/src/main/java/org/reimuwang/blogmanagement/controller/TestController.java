package org.reimuwang.blogmanagement.controller;

import lombok.extern.slf4j.Slf4j;
import org.reimuwang.commonability.http.ModelAndViewHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@Slf4j
public class TestController {

    @GetMapping("/test")
    @ResponseBody
    public ModelAndView test() {
        return ModelAndViewHandler.init("test")
                                  .add("data", "第一个")
                                  .get();
    }
}
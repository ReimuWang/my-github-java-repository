package org.reimuwang.filemanagement.controller.es;

import lombok.extern.slf4j.Slf4j;
import org.reimuwang.commonability.http.CommonResponse;
import org.reimuwang.filemanagement.service.es.EsDataManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Slf4j
@RequestMapping("/es")
public class EsDataManageController {

    @Autowired
    private EsDataManageService esDataManageService;

    @RequestMapping("/refresh/all")
    @ResponseBody
    public CommonResponse refreshAll () {
        String logMark = "[" + System.currentTimeMillis() + "][刷新ES中的所有数据]";
        log.info(logMark + "接到刷新Es中所有数据的http请求");
        this.esDataManageService.refreshAll(logMark);
        return CommonResponse.success("任务-[刷新ES中的所有数据] 开始执行");
    }


}
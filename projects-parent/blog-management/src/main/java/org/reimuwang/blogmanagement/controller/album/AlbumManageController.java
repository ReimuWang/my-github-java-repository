package org.reimuwang.blogmanagement.controller.album;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.reimuwang.blogmanagement.service.album.AlbumManageService;
import org.reimuwang.commonability.http.CommonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Slf4j
@RequestMapping("/album")
public class AlbumManageController {

    @Autowired
    private AlbumManageService albumManageService;

    @RequestMapping("/json/generate")
    @ResponseBody
    public CommonResponse jsonGenerate () {
        String logMark = "[" + System.currentTimeMillis() + "][生成相册json文件]";
        log.info(logMark + "接到http请求");
        try {
            JSONObject result = this.albumManageService.jsonGenerate(logMark);
            return CommonResponse.success(result, null);
        } catch (Exception e) {
            return CommonResponse.error(e.getMessage());
        }
    }

}
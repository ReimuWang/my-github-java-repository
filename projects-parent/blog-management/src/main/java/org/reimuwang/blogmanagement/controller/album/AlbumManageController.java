package org.reimuwang.blogmanagement.controller.album;

import lombok.extern.slf4j.Slf4j;
import org.reimuwang.blogmanagement.service.album.AlbumManageService;
import org.reimuwang.commonability.http.CommonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Slf4j
@RequestMapping("/album")
public class AlbumManageController {

    @Autowired
    private AlbumManageService albumManageService;

    @RequestMapping("/json/generate")
    @ResponseBody
    public CommonResponse jsonGenerate() {
        String logMark = "[" + System.currentTimeMillis() + "][生成相册json文件]";
        log.info(logMark + "接到http请求");
        try {
            return CommonResponse.success(this.albumManageService.jsonGenerate(logMark), null);
        } catch (Exception e) {
            log.error(logMark + "执行出错", e);
            return CommonResponse.error("执行出错，" + e.getMessage());
        }
    }

    @RequestMapping("/image/compressAndCopy")
    @ResponseBody
    public CommonResponse imageCompressAndCopy(@RequestParam(value = "photoQuality", required = false) Float photoQuality) {
        String logMark = "[" + System.currentTimeMillis() + "][图片压缩复制]";
        log.info(logMark + "接到http请求,photoQuality=" + photoQuality);
        try {
            if (this.albumManageService.imageCompressAndCopy(photoQuality, logMark)) {
                return CommonResponse.success();
            } else {
                return CommonResponse.error();
            }
        } catch (Exception e) {
            log.error(logMark + "执行出错", e);
            return CommonResponse.error("执行出错，" + e.getMessage());
        }
    }

    @RequestMapping("/image/upload")
    @ResponseBody
    public CommonResponse imageUpload() {
        String logMark = "[" + System.currentTimeMillis() + "][图片上传]";
        log.info(logMark + "接到http请求");
        try {
            if (this.albumManageService.imageUpload(logMark)) {
                return CommonResponse.success();
            } else {
                return CommonResponse.error();
            }
        } catch (Exception e) {
            log.error(logMark + "执行出错", e);
            return CommonResponse.error("执行出错，" + e.getMessage());
        }
    }

}
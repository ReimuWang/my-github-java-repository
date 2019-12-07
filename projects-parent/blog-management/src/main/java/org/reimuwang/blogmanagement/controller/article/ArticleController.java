package org.reimuwang.blogmanagement.controller.article;

import lombok.extern.slf4j.Slf4j;
import org.reimuwang.blogmanagement.entity.article.ArticleEntity;
import org.reimuwang.blogmanagement.entity.article.request.ArticleQueryRequest;
import org.reimuwang.blogmanagement.service.article.ArticleService;
import org.reimuwang.commonability.http.CommonResponse;
import org.reimuwang.commonability.server.CommonListResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Slf4j
@RequestMapping("/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @RequestMapping("/list")
    @ResponseBody
    public CommonResponse getArticleList(@RequestBody(required = false) ArticleQueryRequest articleQueryRequest) {
        String logMark = "[" + System.currentTimeMillis() + "][获取文章列表]";
        log.info(logMark + "接到http请求,articleQueryRequest=" + articleQueryRequest);
        try {
            CommonListResponse<ArticleEntity> result = this.articleService.getArticleList(logMark);
            return CommonResponse.success(result.getDataList(), null, result.getTotalCount());
        } catch (Exception e) {
            log.error(logMark + "执行出错", e);
            return CommonResponse.error("执行出错，" + e.getMessage());
        }
    }

    @RequestMapping("/recover/fileName")
    @ResponseBody
    public CommonResponse recoverFileName(@RequestParam(value = "preview", defaultValue = "true") Boolean preview) {
        String logMark = "[" + System.currentTimeMillis() + "][修复文件名称]";
        log.info(logMark + "接到http请求,preview=" + preview);
        try {
            return CommonResponse.success(this.articleService.recoverFileName(preview, logMark), null);
        } catch (Exception e) {
            log.error(logMark + "执行出错", e);
            return CommonResponse.error("执行出错，" + e.getMessage());
        }
    }
}

package org.reimuwang.blogmanagement.controller.article;

import lombok.extern.slf4j.Slf4j;
import org.reimuwang.blogmanagement.entity.article.ArticleEntity;
import org.reimuwang.blogmanagement.service.check.ArticleService;
import org.reimuwang.commonability.http.CommonResponse;
import org.reimuwang.commonability.server.CommonListResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Slf4j
@RequestMapping("/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @RequestMapping("/list")
    @ResponseBody
    public CommonResponse getArticleList() {
        String logMark = "[" + System.currentTimeMillis() + "][获取文章列表]";
        log.info(logMark + "接到http请求");
        try {
            CommonListResponse<ArticleEntity> result = this.articleService.getArticleList(logMark);
            return CommonResponse.success(result.getDataList(), result.getTotalCount());
        } catch (Exception e) {
            log.error(logMark + "执行出错", e);
            return CommonResponse.error("执行出错，" + e.getMessage());
        }
    }
}

package org.reimuwang.blogmanagement.controller;

import lombok.extern.slf4j.Slf4j;
import org.reimuwang.blogmanagement.entity.article.ArticleEntity;
import org.reimuwang.blogmanagement.entity.article.request.ArticleQueryRequest;
import org.reimuwang.blogmanagement.service.article.ArticleService;
import org.reimuwang.commonability.http.ModelAndViewHandler;
import org.reimuwang.commonability.server.CommonListResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@Slf4j
public class TestController {

    @Autowired
    private ArticleService articleService;

    @GetMapping("/test")
    @ResponseBody
    public ModelAndView test(ArticleQueryRequest articleQueryRequest) throws Exception {
        CommonListResponse<ArticleEntity> serviceResult = this.articleService.getArticleList(articleQueryRequest, "test");
        return ModelAndViewHandler.init("test")
                                  .add("data", serviceResult.getDataList())
                                  .add("count", serviceResult.getTotalCount())
                                  .addPageInfo(articleQueryRequest, serviceResult)
                                  .get();
    }
}
package org.reimuwang.blogmanagement.service.article;

import com.alibaba.fastjson.JSONObject;
import org.reimuwang.blogmanagement.entity.article.ArticleEntity;
import org.reimuwang.commonability.server.CommonListResponse;

public interface ArticleService {

    CommonListResponse<ArticleEntity> getArticleList(String logMark) throws Exception;

    JSONObject recoverFileName(Boolean preview, String logMark) throws Exception;
}

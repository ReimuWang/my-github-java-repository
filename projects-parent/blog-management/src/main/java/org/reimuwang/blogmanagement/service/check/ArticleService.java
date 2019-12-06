package org.reimuwang.blogmanagement.service.check;

import org.reimuwang.blogmanagement.entity.article.ArticleEntity;
import org.reimuwang.commonability.server.CommonListResponse;

public interface ArticleService {

    CommonListResponse<ArticleEntity> getArticleList(String logMark) throws Exception;
}

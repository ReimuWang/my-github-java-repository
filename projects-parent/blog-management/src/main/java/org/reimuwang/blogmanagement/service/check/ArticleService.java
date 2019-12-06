package org.reimuwang.blogmanagement.service.check;

import org.reimuwang.blogmanagement.entity.article.ArticleEntity;

import java.io.FileNotFoundException;
import java.util.List;

public interface ArticleService {

    List<ArticleEntity> getArticleList(String logMark) throws FileNotFoundException;
}

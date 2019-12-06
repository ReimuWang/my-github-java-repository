package org.reimuwang.blogmanagement.service.check.impl;

import lombok.extern.slf4j.Slf4j;
import org.reimuwang.blogmanagement.entity.article.ArticleEntity;
import org.reimuwang.blogmanagement.service.check.ArticleService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ArticleServiceImpl implements ArticleService {

    @Value("${reimuwang.article.articleDir}")
    private String articleDirPath;

    @Override
    public List<ArticleEntity> getArticleList(String logMark) throws FileNotFoundException {
        File articleDir = new File(this.articleDirPath);
        if (!articleDir.exists() || !articleDir.isDirectory()) {
            log.warn("配置文件中设定的文章目录不存在，articleDirPath=" + this.articleDirPath);
            return new ArrayList<>();
        }
        List<ArticleEntity> result = new ArrayList<>();
        for(File article : articleDir.listFiles()) {
            ArticleEntity articleEntity = ArticleEntity.build(article);
            if (null == articleEntity) {
                continue;
            }
            result.add(articleEntity);
        }
        return result;
    }
}

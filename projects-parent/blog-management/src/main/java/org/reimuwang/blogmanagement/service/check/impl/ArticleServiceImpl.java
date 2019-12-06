package org.reimuwang.blogmanagement.service.check.impl;

import lombok.extern.slf4j.Slf4j;
import org.reimuwang.blogmanagement.entity.article.ArticleEntity;
import org.reimuwang.blogmanagement.service.check.ArticleService;
import org.reimuwang.commonability.server.CommonListResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ArticleServiceImpl implements ArticleService {

    @Value("${reimuwang.article.articleDir}")
    private String articleDirPath;

    @Override
    public CommonListResponse<ArticleEntity> getArticleList(String logMark) throws Exception {
        CommonListResponse<ArticleEntity> result = new CommonListResponse<>();
        File articleDir = new File(this.articleDirPath);
        if (!articleDir.exists() || !articleDir.isDirectory()) {
            log.warn("配置文件中设定的文章目录不存在，articleDirPath=" + this.articleDirPath);
            return result;
        }
        List<ArticleEntity> dataList = new ArrayList<>();
        for(File article : articleDir.listFiles()) {
            ArticleEntity articleEntity = ArticleEntity.build(article);
            if (null == articleEntity) {
                continue;
            }
            dataList.add(articleEntity);
        }
        result.setTotalCount(dataList.size());
        result.setDataList(dataList);
        return result;
    }
}
